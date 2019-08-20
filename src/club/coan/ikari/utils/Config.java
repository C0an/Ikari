package club.coan.ikari.utils;

import club.coan.ikari.Ikari;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config extends YamlConfiguration {

    @Getter private String fileName;

    public Config(String fileName) {
        this(fileName, ".yml");
    }

    public Config(String fileName, String fileExtension) {
        this.fileName = fileName + (fileName.endsWith(fileExtension) ? "" : fileExtension);
        this.createFile();
    }

    private void createFile() {
        File folder = Ikari.getInstance().getDataFolder();

        try {
            File file = new File(folder, fileName);

            if (!file.exists()) {
                if (Ikari.getInstance().getResource(fileName) != null) {
                    Ikari.getInstance().saveResource(fileName, false);
                } else {
                    this.save(file);
                }
                this.load(file);
            } else {
                this.load(file);
                this.save(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        File folder = Ikari.getInstance().getDataFolder();
        try {
            save(new File(folder, fileName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}