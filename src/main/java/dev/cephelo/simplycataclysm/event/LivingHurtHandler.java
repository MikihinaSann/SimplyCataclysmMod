package dev.cephelo.simplycataclysm.event;

import dev.cephelo.simplycataclysm.SimplyCataclysm;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = SimplyCataclysm.MODID)
public class LivingHurtHandler {
    @SubscribeEvent
    public static void onLivingHurt(LivingIncomingDamageEvent ev) {
        DamageSource source = ev.getSource();
        float dmgDealt = ev.getAmount();
        LivingEntity target = ev.getEntity();
        if (dmgDealt != 0.0F && !source.is(DamageTypeTags.IS_PROJECTILE) && !source.is(DamageTypeTags.IS_FIRE) && !source.is(DamageTypeTags.IS_EXPLOSION) && (source.getMsgId().equals("player") || source.getMsgId().equals("mob"))) {
            if (source.getDirectEntity() == source.getEntity() && source.getEntity() instanceof LivingEntity attacker) {
                ItemStack attackerStack = attacker.getMainHandItem();
                if (!attackerStack.isEmpty()) {
                    Item attackerItem = attackerStack.getItem();

                    if (attackerItem instanceof IMeleeDamageCallback) {
                        dmgDealt = ((IMeleeDamageCallback) attackerItem).modifyDamageDealt(dmgDealt, source, attacker, target);
                        ev.setAmount(dmgDealt);
                    }
                }
            }
        }
    }
}
