package sen.manaita_plus.client.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import sen.manaita_plus.ManaitaPlus;
import sen.manaita_plus.common.block.data.Data;
import sen.manaita_plus.common.core.ManaitaPlusBlockCore;
import sen.manaita_plus.common.util.ManaitaPlusUtils;

public class ManaitaPlusBlockStateProvider extends BlockStateProvider {

    public ManaitaPlusBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ManaitaPlus.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        VariantBlockStateBuilder builder;

        horizontalBlock(ManaitaPlusBlockCore.HookBlock.get(), state -> {
            String typeName = ManaitaPlusUtils.getTypes1(state.getValue(sen.manaita_plus.common.block.data.Data.TYPES) + 1);
            Direction direction1 = state.getValue(Data.FACING);
            ModelBuilder<BlockModelBuilder>.ElementBuilder modelFile = models().getBuilder("block/hook/hook_block_" + typeName  + "_" + direction1.getName())
                    .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER + "/cube_all")))
                    .texture("all", modLoc("block/hook/fixed_hook_" + typeName))
                    .element();
            modelFile.from(7, 12, 14)
                    .to(9, 14, 16);
            return modelFile.allFaces((direction, faceBuilder) ->
                            faceBuilder.texture("#all").uvs(5, 5, 10, 10))
                    .end();
        });

        builder = getVariantBuilder(ManaitaPlusBlockCore.CraftingBlock.get());

        for (int i = 0; i <= 8; i++) {
            if(i == 0) {
                ModelFile modelFile =  models().getBuilder("crafting_manaita_model")
                        .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER  + "/cube_all")))
                        .texture("particle", modLoc("block/crafting_manaita"));

                builder.partialState()
                        .with(sen.manaita_plus.common.block.data.Data.TYPES, i)
                        .addModels(new ConfiguredModel(modelFile));
                continue;
            }
            String typeName = ManaitaPlusUtils.getTypes1(i);
            ModelFile modelFile =  models().getBuilder("block/crafting/model/crafting_manaita_" + typeName)
                    .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER  + "/cube_all")))
                    .texture("particle", modLoc("block/crafting/crafting_manaita_" + ManaitaPlusUtils.getTypes1(i)));

            builder.partialState()
                    .with(sen.manaita_plus.common.block.data.Data.TYPES, i)
                    .addModels(new ConfiguredModel(modelFile));
        }

        builder = getVariantBuilder(ManaitaPlusBlockCore.FurnaceBlock.get());

        for (int i = 0; i <= 8; i++) {
            if(i == 0) {
                ModelFile modelFile =  models().getBuilder("furnace_manaita_model")
                        .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER  + "/cube_all")))
                        .texture("particle", modLoc("block/furnace_manaita"));

                builder.partialState()
                        .with(sen.manaita_plus.common.block.data.Data.TYPES, i)
                        .addModels(new ConfiguredModel(modelFile));
                continue;
            }
            String typeName = ManaitaPlusUtils.getTypes1(i);
            ModelFile modelFile =  models().getBuilder("block/furnace/model/furnace_manaita_" + typeName)
                    .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER  + "/cube_all")))
                    .texture("particle", modLoc("block/furnace/furnace_manaita_" + ManaitaPlusUtils.getTypes1(i)));

            builder.partialState()
                    .with(sen.manaita_plus.common.block.data.Data.TYPES, i)
                    .addModels(new ConfiguredModel(modelFile));
        }
        builder = getVariantBuilder(ManaitaPlusBlockCore.BrewingBlock.get());

        for (int i = 0; i <= 8; i++) {
            if(i == 0) {
                ModelFile modelFile =  models().getBuilder("brewing_manaita_model")
                        .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER  + "/cube_all")))
                        .texture("particle", modLoc("block/brewing_manaita"));

                builder.partialState()
                        .with(sen.manaita_plus.common.block.data.Data.TYPES, i)
                        .addModels(new ConfiguredModel(modelFile));
                continue;
            }
            String typeName = ManaitaPlusUtils.getTypes1(i);
            ModelFile modelFile =  models().getBuilder("block/brewing/model/brewing_manaita_" + typeName)
                    .parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER  + "/cube_all")))
                    .texture("particle", modLoc("block/brewing/brewing_manaita_" + ManaitaPlusUtils.getTypes1(i)));

            builder.partialState()
                    .with(sen.manaita_plus.common.block.data.Data.TYPES, i)
                    .addModels(new ConfiguredModel(modelFile));
        }
    }
}