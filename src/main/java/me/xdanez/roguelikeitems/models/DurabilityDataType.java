package me.xdanez.roguelikeitems.models;

import me.xdanez.roguelikeitems.RogueLikeItems;
import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;

// Not sure if this is the best way to implement... I don't know much about PersistentDataTypes
public class DurabilityDataType implements PersistentDataType<byte[], Durability> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<Durability> getComplexType() {
        return Durability.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull Durability durability, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return SerializationUtils.serialize(durability);
    }

    @Override
    public @NotNull Durability fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        try {
            InputStream is = new ByteArrayInputStream(primitive);
            ObjectInputStream o = new ObjectInputStream(is);
            return (Durability) o.readObject();
        } catch (IOException | ClassNotFoundException e) {
            RogueLikeItems.logger().severe(Arrays.toString(e.getStackTrace()));
        }
        // this is stupid
        return null;
    }
}
