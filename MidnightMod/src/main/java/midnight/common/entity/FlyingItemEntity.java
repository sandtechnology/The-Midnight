/*
 * Copyright (c) 2020 Cryptic Mushroom and contributors
 * This file belongs to the Midnight mod and is licensed under the terms and conditions of Cryptic Mushroom. See
 * https://github.com/Cryptic-Mushroom/The-Midnight/blob/rewrite/LICENSE.md for the full license.
 *
 * Last updated: 2020 - 10 - 25
 */

package midnight.common.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class FlyingItemEntity extends ProjectileItemEntity { // aka IBelieveICanFlyItemEntity
    public FlyingItemEntity(EntityType<? extends FlyingItemEntity> type, World world) {
        super(type, world);
    }

    public FlyingItemEntity(EntityType<? extends FlyingItemEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
    }

    public FlyingItemEntity(EntityType<? extends FlyingItemEntity> type, LivingEntity thrower, World world) {
        super(type, thrower, world);
    }

    @Override
    public void shoot(double dx, double dy, double dz, float speed, float accuracy) {
        Vector3d motion = new Vector3d(dx, dy, dz).normalize().add(
            rand.nextGaussian() * 0.0075 * accuracy,
            rand.nextGaussian() * 0.0075 * accuracy,
            rand.nextGaussian() * 0.0075 * accuracy
        ).scale(speed);
        setMotion(motion);

        float horizSpeed = MathHelper.sqrt(horizontalMag(motion));

        rotationYaw = (float) (MathHelper.atan2(motion.x, motion.z) * (180F / Math.PI));
        rotationPitch = (float) (MathHelper.atan2(motion.y, horizSpeed) * (180F / Math.PI));

        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
    }

    @Override
    protected void onImpact(RayTraceResult rtr) {
        RayTraceResult.Type rtrType = rtr.getType();
        if (rtrType == RayTraceResult.Type.ENTITY) {
            onEntityHit((EntityRayTraceResult) rtr);
        } else if (rtrType == RayTraceResult.Type.BLOCK) {
            onBlockHit((BlockRayTraceResult) rtr);
        }
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult rtr) {
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult rtr) {
        BlockState state = world.getBlockState(rtr.getPos());
        state.onProjectileCollision(world, state, rtr, this);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        // Forge provides us an easy way to send entities to client without restrictions of SSpawnObjectPacket
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
