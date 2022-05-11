package com.yuo.endless.Entity;// Made with Blockbench 4.0.5
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GapingVoidModel extends EntityModel<Entity> {
	private final ModelRenderer gaping_void;
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private final ModelRenderer cube1_r1;
	private final ModelRenderer cube1_r2;
	private final ModelRenderer cube1_r3;
	private final ModelRenderer cube1_r4;
	private final ModelRenderer bone3;
	private final ModelRenderer cube1_r5;
	private final ModelRenderer cube1_r6;
	private final ModelRenderer cube1_r7;
	private final ModelRenderer cube1_r8;
	private final ModelRenderer bone5;
	private final ModelRenderer cube1_r9;
	private final ModelRenderer cube1_r10;
	private final ModelRenderer cube1_r11;
	private final ModelRenderer cube1_r12;
	private final ModelRenderer bone7;
	private final ModelRenderer cube1_r13;
	private final ModelRenderer cube1_r14;
	private final ModelRenderer cube1_r15;
	private final ModelRenderer cube1_r16;
	private final ModelRenderer bone4;
	private final ModelRenderer bone6;
	private final ModelRenderer cube1_r17;
	private final ModelRenderer cube1_r18;
	private final ModelRenderer cube1_r19;
	private final ModelRenderer cube1_r20;
	private final ModelRenderer bone8;
	private final ModelRenderer cube1_r21;
	private final ModelRenderer cube1_r22;
	private final ModelRenderer cube1_r23;
	private final ModelRenderer cube1_r24;
	private final ModelRenderer bone9;
	private final ModelRenderer cube1_r25;
	private final ModelRenderer cube1_r26;
	private final ModelRenderer cube1_r27;
	private final ModelRenderer cube1_r28;
	private final ModelRenderer bone10;
	private final ModelRenderer cube1_r29;
	private final ModelRenderer cube1_r30;
	private final ModelRenderer cube1_r31;
	private final ModelRenderer cube1_r32;
	private final ModelRenderer bone21;
	private final ModelRenderer bone22;
	private final ModelRenderer cube1_r33;
	private final ModelRenderer cube1_r34;
	private final ModelRenderer cube1_r35;
	private final ModelRenderer cube1_r36;
	private final ModelRenderer bone23;
	private final ModelRenderer cube1_r37;
	private final ModelRenderer cube1_r38;
	private final ModelRenderer cube1_r39;
	private final ModelRenderer cube1_r40;
	private final ModelRenderer bone24;
	private final ModelRenderer cube1_r41;
	private final ModelRenderer cube1_r42;
	private final ModelRenderer cube1_r43;
	private final ModelRenderer cube1_r44;
	private final ModelRenderer bone25;
	private final ModelRenderer cube1_r45;
	private final ModelRenderer cube1_r46;
	private final ModelRenderer cube1_r47;
	private final ModelRenderer cube1_r48;
	private final ModelRenderer bone26;
	private final ModelRenderer bone27;
	private final ModelRenderer cube1_r49;
	private final ModelRenderer cube1_r50;
	private final ModelRenderer cube1_r51;
	private final ModelRenderer cube1_r52;
	private final ModelRenderer bone28;
	private final ModelRenderer cube1_r53;
	private final ModelRenderer cube1_r54;
	private final ModelRenderer cube1_r55;
	private final ModelRenderer cube1_r56;
	private final ModelRenderer bone29;
	private final ModelRenderer cube1_r57;
	private final ModelRenderer cube1_r58;
	private final ModelRenderer cube1_r59;
	private final ModelRenderer cube1_r60;
	private final ModelRenderer bone30;
	private final ModelRenderer cube1_r61;
	private final ModelRenderer cube1_r62;
	private final ModelRenderer cube1_r63;
	private final ModelRenderer cube1_r64;
	private final ModelRenderer bone31;
	private final ModelRenderer bone32;
	private final ModelRenderer cube1_r65;
	private final ModelRenderer cube1_r66;
	private final ModelRenderer cube1_r67;
	private final ModelRenderer cube1_r68;
	private final ModelRenderer bone33;
	private final ModelRenderer cube1_r69;
	private final ModelRenderer cube1_r70;
	private final ModelRenderer cube1_r71;
	private final ModelRenderer cube1_r72;
	private final ModelRenderer bone34;
	private final ModelRenderer cube1_r73;
	private final ModelRenderer cube1_r74;
	private final ModelRenderer cube1_r75;
	private final ModelRenderer cube1_r76;
	private final ModelRenderer bone35;
	private final ModelRenderer cube1_r77;
	private final ModelRenderer cube1_r78;
	private final ModelRenderer cube1_r79;
	private final ModelRenderer cube1_r80;
	private final ModelRenderer bone36;
	private final ModelRenderer bone37;
	private final ModelRenderer cube1_r81;
	private final ModelRenderer cube1_r82;
	private final ModelRenderer cube1_r83;
	private final ModelRenderer cube1_r84;
	private final ModelRenderer bone38;
	private final ModelRenderer cube1_r85;
	private final ModelRenderer cube1_r86;
	private final ModelRenderer cube1_r87;
	private final ModelRenderer cube1_r88;
	private final ModelRenderer bone39;
	private final ModelRenderer cube1_r89;
	private final ModelRenderer cube1_r90;
	private final ModelRenderer cube1_r91;
	private final ModelRenderer cube1_r92;
	private final ModelRenderer bone40;
	private final ModelRenderer cube1_r93;
	private final ModelRenderer cube1_r94;
	private final ModelRenderer cube1_r95;
	private final ModelRenderer cube1_r96;
	private final ModelRenderer bone11;
	private final ModelRenderer bone12;
	private final ModelRenderer cube1_r97;
	private final ModelRenderer cube1_r98;
	private final ModelRenderer cube1_r99;
	private final ModelRenderer cube1_r100;
	private final ModelRenderer bone13;
	private final ModelRenderer cube1_r101;
	private final ModelRenderer cube1_r102;
	private final ModelRenderer cube1_r103;
	private final ModelRenderer cube1_r104;
	private final ModelRenderer bone14;
	private final ModelRenderer cube1_r105;
	private final ModelRenderer cube1_r106;
	private final ModelRenderer cube1_r107;
	private final ModelRenderer cube1_r108;
	private final ModelRenderer bone15;
	private final ModelRenderer cube1_r109;
	private final ModelRenderer cube1_r110;
	private final ModelRenderer cube1_r111;
	private final ModelRenderer cube1_r112;
	private final ModelRenderer bone16;
	private final ModelRenderer bone17;
	private final ModelRenderer cube1_r113;
	private final ModelRenderer cube1_r114;
	private final ModelRenderer cube1_r115;
	private final ModelRenderer cube1_r116;
	private final ModelRenderer bone18;
	private final ModelRenderer cube1_r117;
	private final ModelRenderer cube1_r118;
	private final ModelRenderer cube1_r119;
	private final ModelRenderer cube1_r120;
	private final ModelRenderer bone19;
	private final ModelRenderer cube1_r121;
	private final ModelRenderer cube1_r122;
	private final ModelRenderer cube1_r123;
	private final ModelRenderer cube1_r124;
	private final ModelRenderer bone20;
	private final ModelRenderer cube1_r125;
	private final ModelRenderer cube1_r126;
	private final ModelRenderer cube1_r127;
	private final ModelRenderer cube1_r128;

	public GapingVoidModel() {
		textureWidth = 16;
		textureHeight = 16;

		gaping_void = new ModelRenderer(this);
		gaping_void.setRotationPoint(0.0F, 16.0F, 0.0F);
		

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 0.0F, 0.0F);
		gaping_void.addChild(bone);
		

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, 1.0F, 0.0F);
		bone.addChild(bone2);
		setRotationAngle(bone2, -1.5708F, 0.0F, 0.0F);
		bone2.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, 7.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r1 = new ModelRenderer(this);
		cube1_r1.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone2.addChild(cube1_r1);
		setRotationAngle(cube1_r1, 0.0F, -0.7854F, 0.0F);
		cube1_r1.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r2 = new ModelRenderer(this);
		cube1_r2.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone2.addChild(cube1_r2);
		setRotationAngle(cube1_r2, 0.0F, -1.1781F, 0.0F);
		cube1_r2.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r3 = new ModelRenderer(this);
		cube1_r3.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone2.addChild(cube1_r3);
		setRotationAngle(cube1_r3, 0.0F, -0.3927F, 0.0F);
		cube1_r3.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r4 = new ModelRenderer(this);
		cube1_r4.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone2.addChild(cube1_r4);
		setRotationAngle(cube1_r4, 0.0F, -1.5708F, 0.0F);
		cube1_r4.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone.addChild(bone3);
		setRotationAngle(bone3, 1.5708F, 0.0F, 0.0F);
		bone3.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r5 = new ModelRenderer(this);
		cube1_r5.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone3.addChild(cube1_r5);
		setRotationAngle(cube1_r5, 0.0F, -0.7854F, 0.0F);
		cube1_r5.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r6 = new ModelRenderer(this);
		cube1_r6.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone3.addChild(cube1_r6);
		setRotationAngle(cube1_r6, 0.0F, -1.1781F, 0.0F);
		cube1_r6.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r7 = new ModelRenderer(this);
		cube1_r7.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone3.addChild(cube1_r7);
		setRotationAngle(cube1_r7, 0.0F, -0.3927F, 0.0F);
		cube1_r7.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r8 = new ModelRenderer(this);
		cube1_r8.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone3.addChild(cube1_r8);
		setRotationAngle(cube1_r8, 0.0F, -1.5708F, 0.0F);
		cube1_r8.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone.addChild(bone5);
		setRotationAngle(bone5, 1.5708F, 0.0F, 1.5708F);
		bone5.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r9 = new ModelRenderer(this);
		cube1_r9.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone5.addChild(cube1_r9);
		setRotationAngle(cube1_r9, 0.0F, -0.7854F, 0.0F);
		cube1_r9.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r10 = new ModelRenderer(this);
		cube1_r10.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone5.addChild(cube1_r10);
		setRotationAngle(cube1_r10, 0.0F, -1.1781F, 0.0F);
		cube1_r10.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r11 = new ModelRenderer(this);
		cube1_r11.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone5.addChild(cube1_r11);
		setRotationAngle(cube1_r11, 0.0F, -0.3927F, 0.0F);
		cube1_r11.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r12 = new ModelRenderer(this);
		cube1_r12.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone5.addChild(cube1_r12);
		setRotationAngle(cube1_r12, 0.0F, -1.5708F, 0.0F);
		cube1_r12.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone7 = new ModelRenderer(this);
		bone7.setRotationPoint(0.0F, 0.0F, -2.0F);
		bone.addChild(bone7);
		setRotationAngle(bone7, -1.5708F, 0.0F, -1.5708F);
		bone7.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r13 = new ModelRenderer(this);
		cube1_r13.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone7.addChild(cube1_r13);
		setRotationAngle(cube1_r13, 0.0F, -0.7854F, 0.0F);
		cube1_r13.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r14 = new ModelRenderer(this);
		cube1_r14.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone7.addChild(cube1_r14);
		setRotationAngle(cube1_r14, 0.0F, -1.1781F, 0.0F);
		cube1_r14.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r15 = new ModelRenderer(this);
		cube1_r15.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone7.addChild(cube1_r15);
		setRotationAngle(cube1_r15, 0.0F, -0.3927F, 0.0F);
		cube1_r15.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r16 = new ModelRenderer(this);
		cube1_r16.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone7.addChild(cube1_r16);
		setRotationAngle(cube1_r16, 0.0F, -1.5708F, 0.0F);
		cube1_r16.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(0.0F, 0.0F, 0.0F);
		gaping_void.addChild(bone4);
		setRotationAngle(bone4, 0.0F, -0.3927F, 0.0F);
		

		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(0.0F, 1.0F, 0.0F);
		bone4.addChild(bone6);
		setRotationAngle(bone6, -1.5708F, 0.0F, 0.0F);
		bone6.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, 7.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r17 = new ModelRenderer(this);
		cube1_r17.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone6.addChild(cube1_r17);
		setRotationAngle(cube1_r17, 0.0F, -0.7854F, 0.0F);
		cube1_r17.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r18 = new ModelRenderer(this);
		cube1_r18.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone6.addChild(cube1_r18);
		setRotationAngle(cube1_r18, 0.0F, -1.1781F, 0.0F);
		cube1_r18.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r19 = new ModelRenderer(this);
		cube1_r19.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone6.addChild(cube1_r19);
		setRotationAngle(cube1_r19, 0.0F, -0.3927F, 0.0F);
		cube1_r19.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r20 = new ModelRenderer(this);
		cube1_r20.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone6.addChild(cube1_r20);
		setRotationAngle(cube1_r20, 0.0F, -1.5708F, 0.0F);
		cube1_r20.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone8 = new ModelRenderer(this);
		bone8.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone4.addChild(bone8);
		setRotationAngle(bone8, 1.5708F, 0.0F, 0.0F);
		bone8.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r21 = new ModelRenderer(this);
		cube1_r21.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone8.addChild(cube1_r21);
		setRotationAngle(cube1_r21, 0.0F, -0.7854F, 0.0F);
		cube1_r21.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r22 = new ModelRenderer(this);
		cube1_r22.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone8.addChild(cube1_r22);
		setRotationAngle(cube1_r22, 0.0F, -1.1781F, 0.0F);
		cube1_r22.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r23 = new ModelRenderer(this);
		cube1_r23.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone8.addChild(cube1_r23);
		setRotationAngle(cube1_r23, 0.0F, -0.3927F, 0.0F);
		cube1_r23.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r24 = new ModelRenderer(this);
		cube1_r24.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone8.addChild(cube1_r24);
		setRotationAngle(cube1_r24, 0.0F, -1.5708F, 0.0F);
		cube1_r24.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone9 = new ModelRenderer(this);
		bone9.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone4.addChild(bone9);
		setRotationAngle(bone9, 1.5708F, 0.0F, 1.5708F);
		bone9.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r25 = new ModelRenderer(this);
		cube1_r25.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone9.addChild(cube1_r25);
		setRotationAngle(cube1_r25, 0.0F, -0.7854F, 0.0F);
		cube1_r25.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r26 = new ModelRenderer(this);
		cube1_r26.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone9.addChild(cube1_r26);
		setRotationAngle(cube1_r26, 0.0F, -1.1781F, 0.0F);
		cube1_r26.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r27 = new ModelRenderer(this);
		cube1_r27.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone9.addChild(cube1_r27);
		setRotationAngle(cube1_r27, 0.0F, -0.3927F, 0.0F);
		cube1_r27.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r28 = new ModelRenderer(this);
		cube1_r28.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone9.addChild(cube1_r28);
		setRotationAngle(cube1_r28, 0.0F, -1.5708F, 0.0F);
		cube1_r28.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone10 = new ModelRenderer(this);
		bone10.setRotationPoint(0.0F, 0.0F, -2.0F);
		bone4.addChild(bone10);
		setRotationAngle(bone10, -1.5708F, 0.0F, -1.5708F);
		bone10.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r29 = new ModelRenderer(this);
		cube1_r29.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone10.addChild(cube1_r29);
		setRotationAngle(cube1_r29, 0.0F, -0.7854F, 0.0F);
		cube1_r29.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r30 = new ModelRenderer(this);
		cube1_r30.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone10.addChild(cube1_r30);
		setRotationAngle(cube1_r30, 0.0F, -1.1781F, 0.0F);
		cube1_r30.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r31 = new ModelRenderer(this);
		cube1_r31.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone10.addChild(cube1_r31);
		setRotationAngle(cube1_r31, 0.0F, -0.3927F, 0.0F);
		cube1_r31.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r32 = new ModelRenderer(this);
		cube1_r32.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone10.addChild(cube1_r32);
		setRotationAngle(cube1_r32, 0.0F, -1.5708F, 0.0F);
		cube1_r32.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone21 = new ModelRenderer(this);
		bone21.setRotationPoint(0.0F, 0.0F, 0.0F);
		gaping_void.addChild(bone21);
		setRotationAngle(bone21, 0.0F, -0.7854F, 0.0F);
		

		bone22 = new ModelRenderer(this);
		bone22.setRotationPoint(0.0F, 1.0F, 0.0F);
		bone21.addChild(bone22);
		setRotationAngle(bone22, -1.5708F, 0.0F, 0.0F);
		bone22.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, 7.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r33 = new ModelRenderer(this);
		cube1_r33.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone22.addChild(cube1_r33);
		setRotationAngle(cube1_r33, 0.0F, -0.7854F, 0.0F);
		cube1_r33.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r34 = new ModelRenderer(this);
		cube1_r34.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone22.addChild(cube1_r34);
		setRotationAngle(cube1_r34, 0.0F, -1.1781F, 0.0F);
		cube1_r34.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r35 = new ModelRenderer(this);
		cube1_r35.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone22.addChild(cube1_r35);
		setRotationAngle(cube1_r35, 0.0F, -0.3927F, 0.0F);
		cube1_r35.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r36 = new ModelRenderer(this);
		cube1_r36.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone22.addChild(cube1_r36);
		setRotationAngle(cube1_r36, 0.0F, -1.5708F, 0.0F);
		cube1_r36.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone23 = new ModelRenderer(this);
		bone23.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone21.addChild(bone23);
		setRotationAngle(bone23, 1.5708F, 0.0F, 0.0F);
		bone23.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r37 = new ModelRenderer(this);
		cube1_r37.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone23.addChild(cube1_r37);
		setRotationAngle(cube1_r37, 0.0F, -0.7854F, 0.0F);
		cube1_r37.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r38 = new ModelRenderer(this);
		cube1_r38.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone23.addChild(cube1_r38);
		setRotationAngle(cube1_r38, 0.0F, -1.1781F, 0.0F);
		cube1_r38.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r39 = new ModelRenderer(this);
		cube1_r39.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone23.addChild(cube1_r39);
		setRotationAngle(cube1_r39, 0.0F, -0.3927F, 0.0F);
		cube1_r39.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r40 = new ModelRenderer(this);
		cube1_r40.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone23.addChild(cube1_r40);
		setRotationAngle(cube1_r40, 0.0F, -1.5708F, 0.0F);
		cube1_r40.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone24 = new ModelRenderer(this);
		bone24.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone21.addChild(bone24);
		setRotationAngle(bone24, 1.5708F, 0.0F, 1.5708F);
		bone24.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r41 = new ModelRenderer(this);
		cube1_r41.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone24.addChild(cube1_r41);
		setRotationAngle(cube1_r41, 0.0F, -0.7854F, 0.0F);
		cube1_r41.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r42 = new ModelRenderer(this);
		cube1_r42.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone24.addChild(cube1_r42);
		setRotationAngle(cube1_r42, 0.0F, -1.1781F, 0.0F);
		cube1_r42.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r43 = new ModelRenderer(this);
		cube1_r43.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone24.addChild(cube1_r43);
		setRotationAngle(cube1_r43, 0.0F, -0.3927F, 0.0F);
		cube1_r43.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r44 = new ModelRenderer(this);
		cube1_r44.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone24.addChild(cube1_r44);
		setRotationAngle(cube1_r44, 0.0F, -1.5708F, 0.0F);
		cube1_r44.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone25 = new ModelRenderer(this);
		bone25.setRotationPoint(0.0F, 0.0F, -2.0F);
		bone21.addChild(bone25);
		setRotationAngle(bone25, -1.5708F, 0.0F, -1.5708F);
		bone25.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r45 = new ModelRenderer(this);
		cube1_r45.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone25.addChild(cube1_r45);
		setRotationAngle(cube1_r45, 0.0F, -0.7854F, 0.0F);
		cube1_r45.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r46 = new ModelRenderer(this);
		cube1_r46.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone25.addChild(cube1_r46);
		setRotationAngle(cube1_r46, 0.0F, -1.1781F, 0.0F);
		cube1_r46.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r47 = new ModelRenderer(this);
		cube1_r47.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone25.addChild(cube1_r47);
		setRotationAngle(cube1_r47, 0.0F, -0.3927F, 0.0F);
		cube1_r47.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r48 = new ModelRenderer(this);
		cube1_r48.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone25.addChild(cube1_r48);
		setRotationAngle(cube1_r48, 0.0F, -1.5708F, 0.0F);
		cube1_r48.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone26 = new ModelRenderer(this);
		bone26.setRotationPoint(0.0F, 0.0F, 0.0F);
		gaping_void.addChild(bone26);
		setRotationAngle(bone26, 0.0F, -1.1781F, 0.0F);
		

		bone27 = new ModelRenderer(this);
		bone27.setRotationPoint(0.0F, 1.0F, 0.0F);
		bone26.addChild(bone27);
		setRotationAngle(bone27, -1.5708F, 0.0F, 0.0F);
		bone27.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, 7.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r49 = new ModelRenderer(this);
		cube1_r49.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone27.addChild(cube1_r49);
		setRotationAngle(cube1_r49, 0.0F, -0.7854F, 0.0F);
		cube1_r49.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r50 = new ModelRenderer(this);
		cube1_r50.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone27.addChild(cube1_r50);
		setRotationAngle(cube1_r50, 0.0F, -1.1781F, 0.0F);
		cube1_r50.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r51 = new ModelRenderer(this);
		cube1_r51.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone27.addChild(cube1_r51);
		setRotationAngle(cube1_r51, 0.0F, -0.3927F, 0.0F);
		cube1_r51.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r52 = new ModelRenderer(this);
		cube1_r52.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone27.addChild(cube1_r52);
		setRotationAngle(cube1_r52, 0.0F, -1.5708F, 0.0F);
		cube1_r52.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone28 = new ModelRenderer(this);
		bone28.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone26.addChild(bone28);
		setRotationAngle(bone28, 1.5708F, 0.0F, 0.0F);
		bone28.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r53 = new ModelRenderer(this);
		cube1_r53.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone28.addChild(cube1_r53);
		setRotationAngle(cube1_r53, 0.0F, -0.7854F, 0.0F);
		cube1_r53.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r54 = new ModelRenderer(this);
		cube1_r54.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone28.addChild(cube1_r54);
		setRotationAngle(cube1_r54, 0.0F, -1.1781F, 0.0F);
		cube1_r54.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r55 = new ModelRenderer(this);
		cube1_r55.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone28.addChild(cube1_r55);
		setRotationAngle(cube1_r55, 0.0F, -0.3927F, 0.0F);
		cube1_r55.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r56 = new ModelRenderer(this);
		cube1_r56.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone28.addChild(cube1_r56);
		setRotationAngle(cube1_r56, 0.0F, -1.5708F, 0.0F);
		cube1_r56.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone29 = new ModelRenderer(this);
		bone29.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone26.addChild(bone29);
		setRotationAngle(bone29, 1.5708F, 0.0F, 1.5708F);
		bone29.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r57 = new ModelRenderer(this);
		cube1_r57.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone29.addChild(cube1_r57);
		setRotationAngle(cube1_r57, 0.0F, -0.7854F, 0.0F);
		cube1_r57.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r58 = new ModelRenderer(this);
		cube1_r58.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone29.addChild(cube1_r58);
		setRotationAngle(cube1_r58, 0.0F, -1.1781F, 0.0F);
		cube1_r58.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r59 = new ModelRenderer(this);
		cube1_r59.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone29.addChild(cube1_r59);
		setRotationAngle(cube1_r59, 0.0F, -0.3927F, 0.0F);
		cube1_r59.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r60 = new ModelRenderer(this);
		cube1_r60.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone29.addChild(cube1_r60);
		setRotationAngle(cube1_r60, 0.0F, -1.5708F, 0.0F);
		cube1_r60.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone30 = new ModelRenderer(this);
		bone30.setRotationPoint(0.0F, 0.0F, -2.0F);
		bone26.addChild(bone30);
		setRotationAngle(bone30, -1.5708F, 0.0F, -1.5708F);
		bone30.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r61 = new ModelRenderer(this);
		cube1_r61.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone30.addChild(cube1_r61);
		setRotationAngle(cube1_r61, 0.0F, -0.7854F, 0.0F);
		cube1_r61.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r62 = new ModelRenderer(this);
		cube1_r62.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone30.addChild(cube1_r62);
		setRotationAngle(cube1_r62, 0.0F, -1.1781F, 0.0F);
		cube1_r62.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r63 = new ModelRenderer(this);
		cube1_r63.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone30.addChild(cube1_r63);
		setRotationAngle(cube1_r63, 0.0F, -0.3927F, 0.0F);
		cube1_r63.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r64 = new ModelRenderer(this);
		cube1_r64.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone30.addChild(cube1_r64);
		setRotationAngle(cube1_r64, 0.0F, -1.5708F, 0.0F);
		cube1_r64.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone31 = new ModelRenderer(this);
		bone31.setRotationPoint(0.0F, 0.0F, 0.0F);
		gaping_void.addChild(bone31);
		setRotationAngle(bone31, 0.0F, -1.5708F, 0.0F);
		

		bone32 = new ModelRenderer(this);
		bone32.setRotationPoint(0.0F, 1.0F, 0.0F);
		bone31.addChild(bone32);
		setRotationAngle(bone32, -1.5708F, 0.0F, 0.0F);
		bone32.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, 7.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r65 = new ModelRenderer(this);
		cube1_r65.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone32.addChild(cube1_r65);
		setRotationAngle(cube1_r65, 0.0F, -0.7854F, 0.0F);
		cube1_r65.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r66 = new ModelRenderer(this);
		cube1_r66.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone32.addChild(cube1_r66);
		setRotationAngle(cube1_r66, 0.0F, -1.1781F, 0.0F);
		cube1_r66.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r67 = new ModelRenderer(this);
		cube1_r67.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone32.addChild(cube1_r67);
		setRotationAngle(cube1_r67, 0.0F, -0.3927F, 0.0F);
		cube1_r67.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r68 = new ModelRenderer(this);
		cube1_r68.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone32.addChild(cube1_r68);
		setRotationAngle(cube1_r68, 0.0F, -1.5708F, 0.0F);
		cube1_r68.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone33 = new ModelRenderer(this);
		bone33.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone31.addChild(bone33);
		setRotationAngle(bone33, 1.5708F, 0.0F, 0.0F);
		bone33.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r69 = new ModelRenderer(this);
		cube1_r69.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone33.addChild(cube1_r69);
		setRotationAngle(cube1_r69, 0.0F, -0.7854F, 0.0F);
		cube1_r69.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r70 = new ModelRenderer(this);
		cube1_r70.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone33.addChild(cube1_r70);
		setRotationAngle(cube1_r70, 0.0F, -1.1781F, 0.0F);
		cube1_r70.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r71 = new ModelRenderer(this);
		cube1_r71.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone33.addChild(cube1_r71);
		setRotationAngle(cube1_r71, 0.0F, -0.3927F, 0.0F);
		cube1_r71.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r72 = new ModelRenderer(this);
		cube1_r72.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone33.addChild(cube1_r72);
		setRotationAngle(cube1_r72, 0.0F, -1.5708F, 0.0F);
		cube1_r72.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone34 = new ModelRenderer(this);
		bone34.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone31.addChild(bone34);
		setRotationAngle(bone34, 1.5708F, 0.0F, 1.5708F);
		bone34.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r73 = new ModelRenderer(this);
		cube1_r73.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone34.addChild(cube1_r73);
		setRotationAngle(cube1_r73, 0.0F, -0.7854F, 0.0F);
		cube1_r73.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r74 = new ModelRenderer(this);
		cube1_r74.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone34.addChild(cube1_r74);
		setRotationAngle(cube1_r74, 0.0F, -1.1781F, 0.0F);
		cube1_r74.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r75 = new ModelRenderer(this);
		cube1_r75.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone34.addChild(cube1_r75);
		setRotationAngle(cube1_r75, 0.0F, -0.3927F, 0.0F);
		cube1_r75.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r76 = new ModelRenderer(this);
		cube1_r76.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone34.addChild(cube1_r76);
		setRotationAngle(cube1_r76, 0.0F, -1.5708F, 0.0F);
		cube1_r76.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone35 = new ModelRenderer(this);
		bone35.setRotationPoint(0.0F, 0.0F, -2.0F);
		bone31.addChild(bone35);
		setRotationAngle(bone35, -1.5708F, 0.0F, -1.5708F);
		bone35.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r77 = new ModelRenderer(this);
		cube1_r77.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone35.addChild(cube1_r77);
		setRotationAngle(cube1_r77, 0.0F, -0.7854F, 0.0F);
		cube1_r77.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r78 = new ModelRenderer(this);
		cube1_r78.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone35.addChild(cube1_r78);
		setRotationAngle(cube1_r78, 0.0F, -1.1781F, 0.0F);
		cube1_r78.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r79 = new ModelRenderer(this);
		cube1_r79.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone35.addChild(cube1_r79);
		setRotationAngle(cube1_r79, 0.0F, -0.3927F, 0.0F);
		cube1_r79.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r80 = new ModelRenderer(this);
		cube1_r80.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone35.addChild(cube1_r80);
		setRotationAngle(cube1_r80, 0.0F, -1.5708F, 0.0F);
		cube1_r80.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone36 = new ModelRenderer(this);
		bone36.setRotationPoint(0.0F, 0.0F, 0.0F);
		gaping_void.addChild(bone36);
		setRotationAngle(bone36, 0.0F, 0.3927F, 0.0F);
		

		bone37 = new ModelRenderer(this);
		bone37.setRotationPoint(0.0F, 1.0F, 0.0F);
		bone36.addChild(bone37);
		setRotationAngle(bone37, -1.5708F, 0.0F, 0.0F);
		bone37.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, 7.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r81 = new ModelRenderer(this);
		cube1_r81.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone37.addChild(cube1_r81);
		setRotationAngle(cube1_r81, 0.0F, -0.7854F, 0.0F);
		cube1_r81.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r82 = new ModelRenderer(this);
		cube1_r82.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone37.addChild(cube1_r82);
		setRotationAngle(cube1_r82, 0.0F, -1.1781F, 0.0F);
		cube1_r82.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r83 = new ModelRenderer(this);
		cube1_r83.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone37.addChild(cube1_r83);
		setRotationAngle(cube1_r83, 0.0F, -0.3927F, 0.0F);
		cube1_r83.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r84 = new ModelRenderer(this);
		cube1_r84.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone37.addChild(cube1_r84);
		setRotationAngle(cube1_r84, 0.0F, -1.5708F, 0.0F);
		cube1_r84.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone38 = new ModelRenderer(this);
		bone38.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone36.addChild(bone38);
		setRotationAngle(bone38, 1.5708F, 0.0F, 0.0F);
		bone38.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r85 = new ModelRenderer(this);
		cube1_r85.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone38.addChild(cube1_r85);
		setRotationAngle(cube1_r85, 0.0F, -0.7854F, 0.0F);
		cube1_r85.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r86 = new ModelRenderer(this);
		cube1_r86.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone38.addChild(cube1_r86);
		setRotationAngle(cube1_r86, 0.0F, -1.1781F, 0.0F);
		cube1_r86.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r87 = new ModelRenderer(this);
		cube1_r87.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone38.addChild(cube1_r87);
		setRotationAngle(cube1_r87, 0.0F, -0.3927F, 0.0F);
		cube1_r87.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r88 = new ModelRenderer(this);
		cube1_r88.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone38.addChild(cube1_r88);
		setRotationAngle(cube1_r88, 0.0F, -1.5708F, 0.0F);
		cube1_r88.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone39 = new ModelRenderer(this);
		bone39.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone36.addChild(bone39);
		setRotationAngle(bone39, 1.5708F, 0.0F, 1.5708F);
		bone39.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r89 = new ModelRenderer(this);
		cube1_r89.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone39.addChild(cube1_r89);
		setRotationAngle(cube1_r89, 0.0F, -0.7854F, 0.0F);
		cube1_r89.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r90 = new ModelRenderer(this);
		cube1_r90.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone39.addChild(cube1_r90);
		setRotationAngle(cube1_r90, 0.0F, -1.1781F, 0.0F);
		cube1_r90.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r91 = new ModelRenderer(this);
		cube1_r91.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone39.addChild(cube1_r91);
		setRotationAngle(cube1_r91, 0.0F, -0.3927F, 0.0F);
		cube1_r91.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r92 = new ModelRenderer(this);
		cube1_r92.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone39.addChild(cube1_r92);
		setRotationAngle(cube1_r92, 0.0F, -1.5708F, 0.0F);
		cube1_r92.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone40 = new ModelRenderer(this);
		bone40.setRotationPoint(0.0F, 0.0F, -2.0F);
		bone36.addChild(bone40);
		setRotationAngle(bone40, -1.5708F, 0.0F, -1.5708F);
		bone40.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r93 = new ModelRenderer(this);
		cube1_r93.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone40.addChild(cube1_r93);
		setRotationAngle(cube1_r93, 0.0F, -0.7854F, 0.0F);
		cube1_r93.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r94 = new ModelRenderer(this);
		cube1_r94.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone40.addChild(cube1_r94);
		setRotationAngle(cube1_r94, 0.0F, -1.1781F, 0.0F);
		cube1_r94.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r95 = new ModelRenderer(this);
		cube1_r95.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone40.addChild(cube1_r95);
		setRotationAngle(cube1_r95, 0.0F, -0.3927F, 0.0F);
		cube1_r95.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r96 = new ModelRenderer(this);
		cube1_r96.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone40.addChild(cube1_r96);
		setRotationAngle(cube1_r96, 0.0F, -1.5708F, 0.0F);
		cube1_r96.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone11 = new ModelRenderer(this);
		bone11.setRotationPoint(0.0F, 0.0F, 0.0F);
		gaping_void.addChild(bone11);
		setRotationAngle(bone11, 0.0F, 0.7854F, 0.0F);
		

		bone12 = new ModelRenderer(this);
		bone12.setRotationPoint(0.0F, 1.0F, 0.0F);
		bone11.addChild(bone12);
		setRotationAngle(bone12, -1.5708F, 0.0F, 0.0F);
		bone12.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, 7.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r97 = new ModelRenderer(this);
		cube1_r97.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone12.addChild(cube1_r97);
		setRotationAngle(cube1_r97, 0.0F, -0.7854F, 0.0F);
		cube1_r97.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r98 = new ModelRenderer(this);
		cube1_r98.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone12.addChild(cube1_r98);
		setRotationAngle(cube1_r98, 0.0F, -1.1781F, 0.0F);
		cube1_r98.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r99 = new ModelRenderer(this);
		cube1_r99.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone12.addChild(cube1_r99);
		setRotationAngle(cube1_r99, 0.0F, -0.3927F, 0.0F);
		cube1_r99.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r100 = new ModelRenderer(this);
		cube1_r100.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone12.addChild(cube1_r100);
		setRotationAngle(cube1_r100, 0.0F, -1.5708F, 0.0F);
		cube1_r100.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone13 = new ModelRenderer(this);
		bone13.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone11.addChild(bone13);
		setRotationAngle(bone13, 1.5708F, 0.0F, 0.0F);
		bone13.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r101 = new ModelRenderer(this);
		cube1_r101.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone13.addChild(cube1_r101);
		setRotationAngle(cube1_r101, 0.0F, -0.7854F, 0.0F);
		cube1_r101.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r102 = new ModelRenderer(this);
		cube1_r102.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone13.addChild(cube1_r102);
		setRotationAngle(cube1_r102, 0.0F, -1.1781F, 0.0F);
		cube1_r102.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r103 = new ModelRenderer(this);
		cube1_r103.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone13.addChild(cube1_r103);
		setRotationAngle(cube1_r103, 0.0F, -0.3927F, 0.0F);
		cube1_r103.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r104 = new ModelRenderer(this);
		cube1_r104.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone13.addChild(cube1_r104);
		setRotationAngle(cube1_r104, 0.0F, -1.5708F, 0.0F);
		cube1_r104.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone14 = new ModelRenderer(this);
		bone14.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone11.addChild(bone14);
		setRotationAngle(bone14, 1.5708F, 0.0F, 1.5708F);
		bone14.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r105 = new ModelRenderer(this);
		cube1_r105.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone14.addChild(cube1_r105);
		setRotationAngle(cube1_r105, 0.0F, -0.7854F, 0.0F);
		cube1_r105.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r106 = new ModelRenderer(this);
		cube1_r106.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone14.addChild(cube1_r106);
		setRotationAngle(cube1_r106, 0.0F, -1.1781F, 0.0F);
		cube1_r106.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r107 = new ModelRenderer(this);
		cube1_r107.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone14.addChild(cube1_r107);
		setRotationAngle(cube1_r107, 0.0F, -0.3927F, 0.0F);
		cube1_r107.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r108 = new ModelRenderer(this);
		cube1_r108.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone14.addChild(cube1_r108);
		setRotationAngle(cube1_r108, 0.0F, -1.5708F, 0.0F);
		cube1_r108.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone15 = new ModelRenderer(this);
		bone15.setRotationPoint(0.0F, 0.0F, -2.0F);
		bone11.addChild(bone15);
		setRotationAngle(bone15, -1.5708F, 0.0F, -1.5708F);
		bone15.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r109 = new ModelRenderer(this);
		cube1_r109.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone15.addChild(cube1_r109);
		setRotationAngle(cube1_r109, 0.0F, -0.7854F, 0.0F);
		cube1_r109.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r110 = new ModelRenderer(this);
		cube1_r110.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone15.addChild(cube1_r110);
		setRotationAngle(cube1_r110, 0.0F, -1.1781F, 0.0F);
		cube1_r110.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r111 = new ModelRenderer(this);
		cube1_r111.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone15.addChild(cube1_r111);
		setRotationAngle(cube1_r111, 0.0F, -0.3927F, 0.0F);
		cube1_r111.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r112 = new ModelRenderer(this);
		cube1_r112.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone15.addChild(cube1_r112);
		setRotationAngle(cube1_r112, 0.0F, -1.5708F, 0.0F);
		cube1_r112.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone16 = new ModelRenderer(this);
		bone16.setRotationPoint(0.0F, 0.0F, 0.0F);
		gaping_void.addChild(bone16);
		setRotationAngle(bone16, 0.0F, 1.1781F, 0.0F);
		

		bone17 = new ModelRenderer(this);
		bone17.setRotationPoint(0.0F, 1.0F, 0.0F);
		bone16.addChild(bone17);
		setRotationAngle(bone17, -1.5708F, 0.0F, 0.0F);
		bone17.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, 7.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r113 = new ModelRenderer(this);
		cube1_r113.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone17.addChild(cube1_r113);
		setRotationAngle(cube1_r113, 0.0F, -0.7854F, 0.0F);
		cube1_r113.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r114 = new ModelRenderer(this);
		cube1_r114.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone17.addChild(cube1_r114);
		setRotationAngle(cube1_r114, 0.0F, -1.1781F, 0.0F);
		cube1_r114.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r115 = new ModelRenderer(this);
		cube1_r115.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone17.addChild(cube1_r115);
		setRotationAngle(cube1_r115, 0.0F, -0.3927F, 0.0F);
		cube1_r115.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r116 = new ModelRenderer(this);
		cube1_r116.setRotationPoint(0.0F, 2.0F, -1.0F);
		bone17.addChild(cube1_r116);
		setRotationAngle(cube1_r116, 0.0F, -1.5708F, 0.0F);
		cube1_r116.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone18 = new ModelRenderer(this);
		bone18.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone16.addChild(bone18);
		setRotationAngle(bone18, 1.5708F, 0.0F, 0.0F);
		bone18.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r117 = new ModelRenderer(this);
		cube1_r117.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone18.addChild(cube1_r117);
		setRotationAngle(cube1_r117, 0.0F, -0.7854F, 0.0F);
		cube1_r117.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r118 = new ModelRenderer(this);
		cube1_r118.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone18.addChild(cube1_r118);
		setRotationAngle(cube1_r118, 0.0F, -1.1781F, 0.0F);
		cube1_r118.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r119 = new ModelRenderer(this);
		cube1_r119.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone18.addChild(cube1_r119);
		setRotationAngle(cube1_r119, 0.0F, -0.3927F, 0.0F);
		cube1_r119.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r120 = new ModelRenderer(this);
		cube1_r120.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone18.addChild(cube1_r120);
		setRotationAngle(cube1_r120, 0.0F, -1.5708F, 0.0F);
		cube1_r120.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone19 = new ModelRenderer(this);
		bone19.setRotationPoint(0.0F, 0.0F, 2.0F);
		bone16.addChild(bone19);
		setRotationAngle(bone19, 1.5708F, 0.0F, 1.5708F);
		bone19.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r121 = new ModelRenderer(this);
		cube1_r121.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone19.addChild(cube1_r121);
		setRotationAngle(cube1_r121, 0.0F, -0.7854F, 0.0F);
		cube1_r121.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r122 = new ModelRenderer(this);
		cube1_r122.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone19.addChild(cube1_r122);
		setRotationAngle(cube1_r122, 0.0F, -1.1781F, 0.0F);
		cube1_r122.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r123 = new ModelRenderer(this);
		cube1_r123.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone19.addChild(cube1_r123);
		setRotationAngle(cube1_r123, 0.0F, -0.3927F, 0.0F);
		cube1_r123.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r124 = new ModelRenderer(this);
		cube1_r124.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone19.addChild(cube1_r124);
		setRotationAngle(cube1_r124, 0.0F, -1.5708F, 0.0F);
		cube1_r124.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		bone20 = new ModelRenderer(this);
		bone20.setRotationPoint(0.0F, 0.0F, -2.0F);
		bone16.addChild(bone20);
		setRotationAngle(bone20, -1.5708F, 0.0F, -1.5708F);
		bone20.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r125 = new ModelRenderer(this);
		cube1_r125.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone20.addChild(cube1_r125);
		setRotationAngle(cube1_r125, 0.0F, -0.7854F, 0.0F);
		cube1_r125.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r126 = new ModelRenderer(this);
		cube1_r126.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone20.addChild(cube1_r126);
		setRotationAngle(cube1_r126, 0.0F, -1.1781F, 0.0F);
		cube1_r126.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r127 = new ModelRenderer(this);
		cube1_r127.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone20.addChild(cube1_r127);
		setRotationAngle(cube1_r127, 0.0F, -0.3927F, 0.0F);
		cube1_r127.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		cube1_r128 = new ModelRenderer(this);
		cube1_r128.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone20.addChild(cube1_r128);
		setRotationAngle(cube1_r128, 0.0F, -1.5708F, 0.0F);
		cube1_r128.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 8.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		gaping_void.render(matrixStack, buffer, packedLight, packedOverlay);
		gaping_void.translateRotate(matrixStack);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}