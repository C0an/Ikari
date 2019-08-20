package club.coan.ikari.faction.claim;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Getter
public class Claim {

    private int x1, y1, z1;
    private int x2, y2, z2;
    private String world;

    public Claim(Location loc1, Location loc2) {
        x1 = loc1.getBlockX();
        y1 = loc1.getBlockY();
        z1 = loc1.getBlockZ();

        x2 = loc2.getBlockX();
        y2 = loc2.getBlockY();
        z2 = loc2.getBlockZ();

        world = loc1.getWorld().getName();
    }

    public Location getCenter() {
        int x1 = this.x2 + 1;
        int y1 = this.y2 + 1;
        int z1 = this.z2 + 1;

        return new Location(Bukkit.getWorld(this.getWorld()), this.x1 + (x1 - this.x1) / 2.0, this.y1 + (y1 - this.y1) / 2.0, this.z1 + (z1 - this.z1) / 2.0);
    }

    public boolean isInside(Location loc, boolean player) {
        int x1 = Math.min(this.getX1(), this.getX2());
        int z1 = Math.min(this.getZ1(), this.getZ2());
        int x2 = Math.max(this.getX1(), this.getX2());
        int z2 = Math.max(this.getZ1(), this.getZ2());
        if (player) {
            ++x2;
            ++z2;
        }
        return loc.getX() >= (double)x1 && loc.getX() <= (double)x2 && loc.getZ() >= (double)z1 && loc.getZ() <= (double)z2;
    }

    public Location pointOne() {
        return new Location(Bukkit.getWorld(world), x1, y1, z1);
    }

    public Location pointTwo() {
        return new Location(Bukkit.getWorld(world), x2, y2, z2);
    }

    public boolean inClaim(Location location) {
        return isInside(location, false);
    }
    public boolean inClaim(Player player) {
        return isInside(player.getLocation(), true);
    }
    public boolean inClaim(Block block) {
        return inClaim(block.getLocation());
    }



}
