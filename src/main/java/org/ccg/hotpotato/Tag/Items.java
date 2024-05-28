package org.ccg.hotpotato.Tag;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.ccg.hotpotato.Tag.Data.TagData;
import org.ccg.hotpotato.Utils.Item.ClickableItem;
import org.ccg.hotpotato.Utils.Item.ItemUtil;
import org.jetbrains.annotations.NotNull;


public class Items {
    private final TagData _data;
    private String gameId = "";

    public Items(@NotNull TagData data) {
        Bukkit.getLogger().info("Creating Items instance with TagData: " + data.getGameID());
        _data = data;
        gameId = _data.getGameID();
    }
    public static final Material HOOK_MATERIAL = Material.FISHING_ROD;
    public final ItemStack HOOK_ItemStack = ItemUtil.generate(HOOK_MATERIAL, 1, true , "&b&lКрюк!");
    
    public static final Material PICKAXE_MATERIAL = Material.NETHERITE_PICKAXE;
    public final ItemStack PICKAXE_ItemStack = ItemUtil.generate(PICKAXE_MATERIAL, 1, "&dКопай!");
    
    public static final Material FEATHER_MATERIAL = Material.FEATHER;
    public ClickableItem GenerateClickableFeather = ClickableItem.of(ItemUtil.generate(FEATHER_MATERIAL, 1, "&f&lПКМ Чтобы взлететь"), this::OnFeatherClicked, gameId);
    
    private void OnFeatherClicked(final Player p)
    {
        Bukkit.getLogger().severe("Feather pressed");
        if (!_data.getCurrentPlayers().contains(p)) return;
        if (_data.getHunters().contains(p)) p.setVelocity(new Vector(0, _data.getConfig().get_ultraFeatherVerticalVelocityForHunters(), 0));
        else p.setVelocity(new Vector(0, _data.getConfig().get_ultraFeatherVerticalVelocityForRunners(), 0));
        ItemUtil.clear(p, FEATHER_MATERIAL);
    }

    
}
