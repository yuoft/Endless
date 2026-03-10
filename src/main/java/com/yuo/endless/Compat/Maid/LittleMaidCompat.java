package com.yuo.endless.Compat.Maid;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;

@LittleMaidExtension
public class LittleMaidCompat implements ILittleMaid {
    // 注册女仆工作任务的方法
    @Override
    public void addMaidTask(TaskManager manager) {
        // 添加自定义任务
        manager.add(new InfinityTask());
    }
}
