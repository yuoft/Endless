package com.yuo.endless.Items.Tool;

import com.yuo.endless.Endless;
import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class Modifiers {
    private static final UUID[] UUID_HEALTH = new UUID[]{
            UUID.fromString("624f740b-6658-4b1a-9ea8-59bce100fe92"),
            UUID.fromString("4898d4a4-64d1-4f2d-a177-09a4ab79db5a"),
            UUID.fromString("667f9d98-8d50-49c8-8653-9bc2f265e8d6"),
            UUID.fromString("2db91fae-60bd-46c2-9f2f-34f50ec2c9da")};
    private static final UUID[] UUID_SWIM = new UUID[]{
            UUID.fromString("731df8f9-0002-4f8c-9c89-529af7de38eb"), //0
            UUID.fromString("bce19f23-b799-4698-b8b5-e50fc5852835"), //1
            UUID.fromString("2d761976-460d-4d86-8d83-2e5a845e79ed"), //2
            UUID.fromString("accadb33-a739-49ed-b95a-71866a7dc47c"), //3
            UUID.fromString("87ce5788-5e82-4f2a-9ce4-67a25a8a690f"), //4
            UUID.fromString("7c96f59b-dee4-4f68-9848-5265b8fef5ea"), //5
            UUID.fromString("6c4bbf8f-626b-4e5f-945d-df5fc3b49bd6"), //6
            UUID.fromString("66a82051-659d-4149-90ae-d2d30e6bbab5"), //7
            UUID.fromString("b3084122-6b3e-48cd-aaba-284d916380a5")}; //8
    private static final UUID[] UUID_SPEED = new UUID[]{
            UUID.fromString("7accc71c-e1ca-4149-a010-d7818fbce503"),
            UUID.fromString("cc6e3f5d-44a5-4801-820f-ae9d76372d98"),
            UUID.fromString("390ed378-ffce-4e5b-aef4-8e33736d560d"),
            UUID.fromString("cf63b2db-f89a-4b49-9f7d-239f5032ac54"),
            UUID.fromString("ef94bdb1-ddda-4235-8994-c8a65db5a764"),
            UUID.fromString("c2f8936e-f18b-4679-b019-16600d354642"),
            UUID.fromString("c4ef17e0-8a62-4f44-92d4-ea0e2170a7db"),
            UUID.fromString("0d47d0e2-fd52-4a7e-a570-3f49e9f20396"),
            UUID.fromString("e484d92e-13cb-46ac-976b-c816ba2f4ba2")};

    public static AttributeModifier getModifierHealth(int type, double value){
        return new AttributeModifier(UUID_HEALTH[type], Endless.MOD_ID + ":max_health", value, AttributeModifier.Operation.ADDITION);
    }

    public static AttributeModifier getModifierSpeed(int type, double value){
        return new AttributeModifier(UUID_SPEED[type], Endless.MOD_ID + ":movement_speed", value, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    public static AttributeModifier getModifierSwim(int type, double value){
        return new AttributeModifier(UUID_SWIM[type], Endless.MOD_ID + ":swim_speed", value, AttributeModifier.Operation.MULTIPLY_BASE);
    }

}
