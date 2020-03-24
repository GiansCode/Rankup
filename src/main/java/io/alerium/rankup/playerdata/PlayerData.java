package io.alerium.rankup.playerdata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor @Getter @Setter
public class PlayerData {
    
    private final UUID uuid;
    private String rank;
    
}
