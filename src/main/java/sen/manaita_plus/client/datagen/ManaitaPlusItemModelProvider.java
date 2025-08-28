package sen.manaita_plus.client.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import sen.manaita_plus.ManaitaPlus;

public class ManaitaPlusItemModelProvider extends ItemModelProvider {

    public ManaitaPlusItemModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ManaitaPlus.MODID, exFileHelper);
    }

    @Override
    protected void registerModels() {
        var builder = withExistingParent("hook_block_item", "item/generated")
                .texture("layer0", modLoc("block/hook/fixed_hook_wooden"));

        // 为不同类型批量添加override
        for (int i = 1; i <= 8; i++) {
            String typeName = getTypeName(i + 1); // 获取类型名称
            builder.override()
                    .predicate(modLoc("manaita_type"), i)
                    .model(withExistingParent("block/hook/fixed_hook_" + typeName, "item/generated")
                            .texture("layer0", modLoc("block/hook/fixed_hook_" + typeName)));
        }

        builder = withExistingParent("block_crafting_manaita", "item/generated")
                .texture("layer0", modLoc("block/crafting_manaita"));

        // 为不同类型批量添加override
        for (int i = 1; i <= 8; i++) {
            String typeName = getTypeName(i); // 获取类型名称
            builder.override()
                    .predicate(modLoc("manaita_type"), i)
                    .model(withExistingParent("crafting_manaita_" + typeName, "item/generated")
                            .texture("layer0", modLoc("block/crafting/crafting_manaita_" + typeName)));
        }

        builder = withExistingParent("block_furnace_manaita", "item/generated")
                .texture("layer0", modLoc("block/furnace_manaita"));

        // 为不同类型批量添加override
        for (int i = 1; i <= 8; i++) {
            String typeName = getTypeName(i); // 获取类型名称
            builder.override()
                    .predicate(modLoc("manaita_type"), i)
                    .model(withExistingParent("furnace_manaita_" + typeName, "item/generated")
                            .texture("layer0", modLoc("block/furnace/furnace_manaita_" + typeName)));
        }

        builder = withExistingParent("block_brewing_manaita", "item/generated")
                .texture("layer0", modLoc("block/brewing_manaita"));

        // 为不同类型批量添加override
        for (int i = 1; i <= 8; i++) {
            String typeName = getTypeName(i); // 获取类型名称
            builder.override()
                    .predicate(modLoc("manaita_type"), i)
                    .model(withExistingParent("brewing_manaita_" + typeName, "item/generated")
                            .texture("layer0", modLoc("block/brewing/brewing_manaita_" + typeName)));
        }
    }

    private String getTypeName(int type) {
        switch (type) {
            case 1: return "wooden";
            case 2: return "stone";
            case 3: return "iron";
            case 4: return "gold";
            case 5: return "diamond";
            case 6: return "emerald";
            case 7: return "redstone";
            case 8: return "netherite";
            default: return "wooden";
        }
    }
}