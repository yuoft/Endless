package com.yuo.endless.Compat.Curios;

import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

public class CuriosCompat {

    public static void sendImc(InterModEnqueueEvent evt){
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BODY.getMessageBuilder().build());
    }
}
