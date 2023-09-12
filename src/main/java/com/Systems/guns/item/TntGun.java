package com.Systems.guns.item;

import com.Systems.guns.entity.ExplosionTridentEntity;
import com.mojang.math.Vector3d;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeSpawnEggItem;
import com.Systems.guns.init.GunItems;
import com.Systems.guns.tab.GunsTab;
import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import oshi.hardware.SoundCard;

import java.util.function.Predicate;

public class TntGun extends ProjectileWeaponItem {
    public static final Predicate<ItemStack> TNTGUN = stack -> stack.getItem() == Items.TNT;
    public TntGun(){
        super(new Item.Properties().tab(GunsTab.GUN_TAB).stacksTo(64).rarity(Rarity.COMMON));

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Vec3 v3 = player.getViewVector(1F);
        ItemStack itemstac = player.getProjectile(stack);
        if(!level.isClientSide && itemstac.getItem() == Items.TNT){
            ItemStack ammo = new ItemStack(Items.AIR);
            AbstractArrow arrow= new ExplosionTridentEntity(level, player, ammo);
            arrow.shoot(v3.x, v3.y,v3.z,45F,0);
            level.addFreshEntity(arrow);
            level.playSound(null, player.getX(),player.getY(),player.getZ(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS,1.0F,1.0F);

            if(!player.getAbilities().instabuild){
                itemstac.shrink(1);
            }
            stack.hurtAndBreak(1,player,(b)->{
                b.broadcastBreakEvent(player.getUsedItemHand());
            });
            return InteractionResultHolder.consume(stack);
        }
        level.playSound(null, player.getX(),player.getY(),player.getZ(), SoundEvents.ITEM_FRAME_BREAK, SoundSource.PLAYERS,1.0F,1.0F);

        return InteractionResultHolder.fail(stack);

    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return TNTGUN;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }
}
