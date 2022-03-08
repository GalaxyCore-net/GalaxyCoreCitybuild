package net.galaxycore.citybuild.shop;

import lombok.Getter;
import org.bukkit.Chunk;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ShopChunkData {
    private final File file;
    private final Chunk chunk;
    private List<Shop> shopsInThisChunk = new ArrayList<>();

    public ShopChunkData(File file, Chunk chunk) {
        this.file = file;
        this.chunk = chunk;
    }

    public void load() {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            //noinspection unchecked
            shopsInThisChunk = (List<Shop>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
    }

    public void save() {
        if(file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(shopsInThisChunk);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
