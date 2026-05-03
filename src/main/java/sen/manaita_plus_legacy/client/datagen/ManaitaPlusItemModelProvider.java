package sen.manaita_plus_legacy.client.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;

public class ManaitaPlusItemModelProvider extends ItemModelProvider {

    public ManaitaPlusItemModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ManaitaPlusLegacy.MODID, exFileHelper);
    }

    @Override
    protected void registerModels() {
      String[] rings = new String[] {"crafting","furnace","brewing"};
        for (String ring : rings) {
            var builder = withExistingParent("manaita_" + ring + "_ring", "item/generated")
                    .texture("layer0", modLoc("item/ring/ring_" + ring + "_manaita"));

            for (int i = 0; i <= 8; i++) {
                String typeName = getTypeName(i);
                builder.override()
                        .predicate(modLoc("manaita_plus_legacy_type"), i)
                        .model(withExistingParent("item/ring/ring_" + ring + "_manaita" + typeName, "item/generated")
                                .texture("layer0", modLoc("item/ring/ring_" + ring + "_manaita" + typeName)));
            }
        }

//        var builder = withExistingParent("block_hook_manaita", "item/generated")
//                .texture("layer0", modLoc("block/hook/fixed_hook_wooden"));
//
//        // 为不同类型批量添加override
//        for (int i = 1; i <= 8; i++) {
//            String typeName = getTypeName(i + 1);
//            builder.override()
//                    .predicate(modLoc("manaita_plus_legacy_type"), i)
//                    .model(withExistingParent("block/hook/fixed_hook_" + typeName, "item/generated")
//                            .texture("layer0", modLoc("block/hook/fixed_hook_" + typeName)));
//        }
//
//        builder = withExistingParent("block_crafting_manaita", "item/generated")
//                .texture("layer0", modLoc("block/crafting_manaita"));
//
//        for (int i = 1; i <= 8; i++) {
//            String typeName = getTypeName(i);
//            builder.override()
//                    .predicate(modLoc("manaita_plus_legacy_type"), i)
//                    .model(withExistingParent("crafting_manaita_" + typeName, "item/generated")
//                            .texture("layer0", modLoc("block/crafting/crafting_manaita_" + typeName)));
//        }
//
//        builder = withExistingParent("block_furnace_manaita", "item/generated")
//                .texture("layer0", modLoc("block/furnace_manaita"));
//
//        for (int i = 1; i <= 8; i++) {
//            String typeName = getTypeName(i);
//            builder.override()
//                    .predicate(modLoc("manaita_plus_legacy_type"), i)
//                    .model(withExistingParent("furnace_manaita_" + typeName, "item/generated")
//                            .texture("layer0", modLoc("block/furnace/furnace_manaita_" + typeName)));
//        }
//
//        builder = withExistingParent("block_brewing_manaita", "item/generated")
//                .texture("layer0", modLoc("block/brewing_manaita"));
//
//        for (int i = 1; i <= 8; i++) {
//            String typeName = getTypeName(i);
//            builder.override()
//                    .predicate(modLoc("manaita_plus_legacy_type"), i)
//                    .model(withExistingParent("brewing_manaita_" + typeName, "item/generated")
//                            .texture("layer0", modLoc("block/brewing/brewing_manaita_" + typeName)));
//        }
    }

    private String getTypeName(int type) {
        return switch (type) {
            case 0 -> "";
            case 1 -> "_wooden";
            case 2 -> "_stone";
            case 3 -> "_iron";
            case 4 -> "_gold";
            case 5 -> "_diamond";
            case 6 -> "_emerald";
            case 7 -> "_redstone";
            case 8 -> "_netherite";
            default -> "_wooden";
        };
    }
}