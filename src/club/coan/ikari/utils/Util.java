package club.coan.ikari.utils;

import net.minecraft.util.com.google.common.reflect.ClassPath;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Util {

    public static void registerListeners(Plugin plugin, String packages) {
        try {
            final ClassPath cp = ClassPath.from(plugin.getClass().getClassLoader());
            final Class[] eventClass = new Class[1];
            final Object[] eventObject = new Object[1];
            final Listener[] listener = new Listener[1];
            cp.getTopLevelClassesRecursive(packages).forEach(classInfo -> {
                try {
                    eventClass[0] = Class.forName(classInfo.getName());
                    eventObject[0] = eventClass[0].newInstance();
                    if(eventObject[0] instanceof Listener) {
                        listener[0] = (Listener) eventObject[0];
                        Bukkit.getServer().getPluginManager().registerEvents(listener[0], plugin);
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex2) {
                    ex2.printStackTrace();
                }
            });
        }catch (Exception e2) {
            e2.printStackTrace();
        }
    }

}
