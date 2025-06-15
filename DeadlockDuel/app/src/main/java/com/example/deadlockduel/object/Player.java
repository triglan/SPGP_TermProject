package com.example.deadlockduel.object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import com.example.deadlockduel.framework.battle.AttackCommand;
import com.example.deadlockduel.framework.battle.AttackEffect;
import com.example.deadlockduel.framework.battle.AttackType;
import com.example.deadlockduel.framework.battle.EffectManager;
import com.example.deadlockduel.framework.core.BlockRectProvider;
import com.example.deadlockduel.framework.data.WeaponDatabase;
import com.example.deadlockduel.framework.data.WeaponInfo;

import com.example.deadlockduel.R;

public class Player {
    private final SpriteFrames sprite;
    private int blockIndex = 0;
    private int drawX, drawY;
    private int direction = 1; // 1 = 오른쪽, -1 = 왼쪽
    private static final int TILE_WIDTH = 100;
    private boolean facingRight = true;
    private int frameTick = 0;
    private final int frameInterval = 8;
    private int hp = 10, maxHp = 10;
    private int blockCount;
    private final Resources res;
    private long lastAttackEffectEndTime = 0;
    private boolean isExecutingQueue = false;
    private final long ATTACK_EFFECT_INTERVAL = 500; // 0.5초
    private BlockRectProvider blockRectProvider;

    // 공격 큐 및 무기 쿨타임 관련 필드
    private final List<AttackCommand> attackQueue = new ArrayList<>();
    private final int[] weaponCooldowns = new int[] {    // 현재 쿨타임
            WeaponDatabase.get(AttackType.MELEE).maxCooldown,
            WeaponDatabase.get(AttackType.LONG_RANGE).maxCooldown,
            WeaponDatabase.get(AttackType.POWER).maxCooldown
    };
    private final int[] maxCooldowns = new int[] {2, 3, 3}; // 무기별 최대 쿨타임
    private final int[] weaponPower = new int[] {4, 2, 3};

    // 타이밍 보너스 판별 위한 이전 입력 시간 저장
    private long lastAttackTime = 0;
    private final long BONUS_WINDOW = 200; // 밀리초


    public Player(Resources res) {
        int[] resIds = {
                R.drawable.player_idle_1,
                R.drawable.player_idle_2,
                R.drawable.player_idle_3,
                R.drawable.player_idle_4
        };
        this.res = res;
        sprite = new SpriteFrames(res, resIds, 1.0f, 0, 0);
    }

    // 무기 사용 시 공격 큐에 등록
    public boolean tryAddAttack(int weaponIndex, long currentTimeMillis) {
        if (weaponCooldowns[weaponIndex] < maxCooldowns[weaponIndex]) {
            return false; // 쿨타임 중
        }
        boolean isBonus = (currentTimeMillis - lastAttackTime <= BONUS_WINDOW);
        AttackType type = AttackType.values()[weaponIndex];
        attackQueue.add(new AttackCommand(type, this, null, isBonus));

        weaponCooldowns[weaponIndex] = 0; // 쿨타임 리셋
        lastAttackTime = currentTimeMillis;

        Log.d("Player", "✅ 공격 큐 추가됨: ...");
        return true;
    }

    //  턴 경과 시 쿨타임 증가
    public void onTurnPassed() {
        // 어떤 무기 인덱스가 큐에 있는지 확인
        Set<Integer> queuedWeaponIndexes = new HashSet<>();
        for (AttackCommand cmd : attackQueue) {
            if (cmd.type != null) {
                queuedWeaponIndexes.add(cmd.type.ordinal());
            }
        }

        // 큐에 없는 무기만 회복
        for (int i = 0; i < weaponCooldowns.length; i++) {
            if (!queuedWeaponIndexes.contains(i) && weaponCooldowns[i] < maxCooldowns[i]) {
                weaponCooldowns[i]++;
            }
        }
    }


    // 쿨타임 확인용 getter
    public int[] getWeaponCooldowns() { return weaponCooldowns; }
    public int[] getMaxCooldowns() {
        return new int[] {
                WeaponDatabase.get(AttackType.MELEE).maxCooldown,
                WeaponDatabase.get(AttackType.LONG_RANGE).maxCooldown,
                WeaponDatabase.get(AttackType.POWER).maxCooldown
        };
    }



    public void setBlockCount(int count) {
        this.blockCount = count;
    }

    public void setBlockIndex(int index) {
        this.blockIndex = index;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public int getDirection() {
        return direction;
    }



    public void updateBlockState(Block[] blocks) {
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].setHasPlayer(i == blockIndex);
        }
    }

    public void updatePositionFromBlock(Rect blockRect) {
        int drawWidth = sprite.getWidth();
        int drawHeight = sprite.getHeight();
        drawX = blockRect.centerX() - drawWidth / 2;
        drawY = blockRect.top - drawHeight;
    }

    public void moveLeft() {
        if (blockIndex > 0) {
            blockIndex--;
            Log.d("Player", "← 이동: blockIndex = " + blockIndex);
        } else {
            Log.d("Player", "왼쪽 끝이라 이동 불가");
        }
    }

    public void moveRight() {
        if (blockIndex < blockCount - 1) {
            blockIndex++;
            Log.d("Player", "→ 이동: blockIndex = " + blockIndex);
        } else {
            Log.d("Player", "오른쪽 끝이라 이동 불가");
        }
    }

    public void reset(int blockIndex, boolean faceRight) {
        this.blockIndex = blockIndex;
        this.direction = faceRight ? 1 : -1;
        this.facingRight = faceRight;
    }

    public void rotate() {
        this.direction *= -1;
        this.facingRight = (direction == 1);
        Log.d("Player", "⟳ 회전: direction = " + direction);
    }

    public void update(List<Enemy> enemies) {
        frameTick++;
        if (frameTick >= frameInterval) {
            frameTick = 0;
            sprite.setFrame(sprite.getFrameIndex() + 1);
        }

        if (isExecutingQueue && System.currentTimeMillis() >= lastAttackEffectEndTime) {
            executeNextAttack(enemies);
        }
    }
    public void setBlockRectProvider(BlockRectProvider provider) {
        this.blockRectProvider = provider;
    }
    public void startAttackQueue() {
        if (!attackQueue.isEmpty()) {
            isExecutingQueue = true;
            lastAttackEffectEndTime = 0; // 첫 공격 즉시 실행
        }
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
        // TODO: 피격 이펙트, 반짝임, 사망 처리 등 추가 가능
    }

    private void executeNextAttack(List<Enemy> enemies) {
        if (attackQueue.isEmpty()) {
            isExecutingQueue = false;
            return;
        }

        AttackCommand cmd = attackQueue.remove(0);
        AttackType type = cmd.type;

        // 가까운 적 찾기
        int playerPos = this.blockIndex;
        int direction = this.direction;
        int range = type.range;

        Enemy closest = null;
        int minDist = Integer.MAX_VALUE;

        for (Enemy enemy : enemies) {
            if (enemy.isDead()) continue;
            int dist = enemy.getBlockIndex() - playerPos;
            if ((direction == 1 && dist > 0 && dist <= range) ||
                    (direction == -1 && dist < 0 && -dist <= range)) {
                if (Math.abs(dist) < minDist) {
                    closest = enemy;
                    minDist = Math.abs(dist);
                }
            }
        }

        // 이펙트 위치용 블럭
        Rect targetBlockRect = null;

        if (closest != null) {
            // ✅ 타겟을 포함한 AttackCommand 생성 후 실행
            AttackCommand actualCmd = new AttackCommand(type, this, closest, cmd.isBonus);
            actualCmd.execute();

            // 이펙트 위치 계산용
            targetBlockRect = blockRectProvider.getBlockRect(closest.getBlockIndex());
        } else {
            int frontBlockIndex = playerPos + direction;
            if (frontBlockIndex >= 0 && frontBlockIndex < blockCount) {
                targetBlockRect = blockRectProvider.getBlockRect(frontBlockIndex);
            }
        }

        // 이펙트 출력
        if (targetBlockRect != null) {
            Bitmap[] frames = type.effectFrames;
            boolean faceRight = type.effectFacesRight;
            int offsetY = type.offsetY;
            float scale = type.effectScale;

            int x = targetBlockRect.centerX() - type.effectFrames[0].getWidth() / 2;
            int y = targetBlockRect.centerY() - type.effectFrames[0].getHeight() / 2 + offsetY;

            EffectManager.getInstance().addEffect(
                    new AttackEffect(frames, x, y, faceRight, scale)
            );
        }

        // 다음 공격 대기 시간 설정
        lastAttackEffectEndTime = System.currentTimeMillis() + ATTACK_EFFECT_INTERVAL;
    }





    public void draw(Canvas canvas) {
        canvas.save();
        if (facingRight) {
            canvas.scale(-1, 1, drawX + sprite.getWidth() / 2f, drawY + sprite.getHeight() / 2f);
        }
        sprite.draw(canvas, drawX, drawY);
        canvas.restore();

        // HP 출력
        Paint hpPaint = new Paint();
        hpPaint.setColor(Color.GREEN);
        hpPaint.setTextAlign(Paint.Align.CENTER);
        hpPaint.setTextSize(28f);
        canvas.drawText("HP: " + this.hp, drawX + sprite.getWidth() / 2f, drawY - 25, hpPaint);
        // HP 바 위치 계산
        int barWidth = sprite.getWidth();
        int barHeight = 8;
        int barX = drawX;
        int barY = drawY - 16; // 머리 위 여백

        // 전체 바 (빨간색)
        Paint backPaint = new Paint();
        backPaint.setColor(Color.RED);
        canvas.drawRect(barX, barY, barX + barWidth, barY + barHeight, backPaint);

        // 남은 체력 바 (초록색)
        //Paint hpPaint = new Paint();
        hpPaint.setColor(Color.GREEN);
        float ratio = (float) hp / maxHp;
        canvas.drawRect(barX, barY, barX + (int)(barWidth * ratio), barY + barHeight, hpPaint);

        // 공격 아이콘
        Bitmap[] attackIcons = new Bitmap[] {
                BitmapFactory.decodeResource(res, R.drawable.attack_icon_melee),
                BitmapFactory.decodeResource(res, R.drawable.attack_icon_range),
                BitmapFactory.decodeResource(res, R.drawable.attack_icon_power),
        };

        int iconSize = sprite.getWidth();              // 아이콘 크기 = 플레이어 너비
        int iconSpacing = iconSize + 8;                // 아이콘 간 간격

        for (int i = 0; i < attackQueue.size(); i++) {
            AttackType type = attackQueue.get(i).type;
            int iconIndex = type.ordinal();
            Bitmap icon = attackIcons[iconIndex];

            int iconX = drawX;                          // 플레이어와 가로 정렬
            int iconY = drawY - 130 - iconSpacing * i;  // 아래에서 위로 쌓기


            if (icon != null) {
                //아이콘 출력
                canvas.drawBitmap(
                        Bitmap.createScaledBitmap(icon, iconSize, iconSize, false),
                        iconX, iconY, null
                );
                // 쿨타임 텍스트 출력
                Paint cooldownPaint = new Paint();
                cooldownPaint.setColor(Color.WHITE);
                cooldownPaint.setTextSize(28f);
                cooldownPaint.setTextAlign(Paint.Align.CENTER);

                int cooldownNow = weaponCooldowns[iconIndex];
                int cooldownMax = maxCooldowns[iconIndex];
                String cooldownText = cooldownNow + "/" + cooldownMax;

                canvas.drawText(
                        cooldownText,
                        iconX + iconSize / 2f,
                        iconY + iconSize + 26,  // 아이콘 아래에 출력
                        cooldownPaint
                );
            } else {
                Paint fallback = new Paint();
                fallback.setColor(Color.MAGENTA);
                canvas.drawRect(iconX, iconY, iconX + iconSize, iconY + iconSize, fallback);
            }
        }


    }
}
