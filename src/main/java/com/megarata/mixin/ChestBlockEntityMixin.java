package com.megarata.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.megarata.server.LootTableCache;

import net.minecraft.block.BlockState;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;

import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;


@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin extends LootableContainerBlockEntity {

    protected ChestBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    private ChestBlockEntity self() {
        return (ChestBlockEntity) (Object) (this);
    }

    @Shadow
    private DefaultedList<ItemStack> inventory;

    @Shadow public abstract void readNbt(NbtCompound nbt);

    private boolean used = getUsed();

    private Random random = new Random();



    //se obtiene la tag used, de los cofres ya cargados del mundo.
    public boolean getUsed(){
        if(hasWorld() && !world.isClient()){
            return self().createNbt().getBoolean("used");
        }
        return false;
    }

    public String getTableString(NbtCompound nbtCompound) {
        if(world != null && !world.isClient){
            //Obtiene la entrada de registro del bioma.
            RegistryKey<Biome> biomeKey = world.getBiome(self().getPos()).getKey().get();
            String biome = biomeKey.getValue().toString();
            //obtiene un JSON ya cargado, solo mandado a cargar mediante el comando, o cuando se inicia el servidor.
            JsonObject json = LootTableCache.getCachedJson();
            //revisa si algunas de las entradas del Json coinciden con las del bioma.
            if(json.has(biome)){
                JsonArray jsonArray = json.getAsJsonArray(biome);
                int toGet = random.nextInt(0, jsonArray.size());
                return jsonArray.get(toGet).getAsString();
            }

            //en caso de no contener en ninguna de las entradas el bioma, se le asigna la entrada "default".
            JsonArray jsonArray = json.getAsJsonArray("default");

            return jsonArray.get(0).getAsString();
        }
        return "";
    }


    //Los cambios son llamados a la hora de leer los tags,
    @Inject(method = "readNbt",at = @At(value = "HEAD"))
    public void readUsedNbt(NbtCompound nbt, CallbackInfo ci){
        used = nbt.getBoolean("used");
        if(!used && self().hasWorld() && !world.isClient){
            NbtCompound selfStoredData = self().createNbt();
            selfStoredData.remove("Items");
        }
        //se agrega una loot table como string para que futuros métodos del mismo cofre generen loot dentro del cofre.
        if(!used && this.lootTableId == null && self().hasWorld() && !world.isClient){
            String lootTableString = getTableString(nbt);
            nbt.putString("LootTable",lootTableString);
            nbt.putLong("LootTableSeed",random.nextLong());
        }
    }

    //Solamente guarda los datos adicionales a la hora de sincronizar los chunks o guardar el mundoi
    @Inject(method = "writeNbt",at = @At("HEAD"))
    public void writeUsedNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("used",used);
    }


    //Añade la tag "used" al momento de abrir el cofre.
    @Inject(method = "onOpen", at = @At("HEAD"))
    public void onOpen(PlayerEntity player, CallbackInfo ci) {
        used = true;
    }

}