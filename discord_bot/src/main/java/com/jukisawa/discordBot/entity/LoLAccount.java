package com.jukisawa.discordBot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "lol_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoLAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long discordUserId;
    private String gameName;
    private String tagLine;
}
