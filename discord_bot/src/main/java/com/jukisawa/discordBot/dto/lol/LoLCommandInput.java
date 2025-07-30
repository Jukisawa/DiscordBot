package com.jukisawa.discordBot.dto.lol;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoLCommandInput {
    private String riotId;
    private Integer gameCount;
    private QueueType queueType;    
}
