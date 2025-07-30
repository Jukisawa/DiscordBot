package com.jukisawa.discordBot.repository;

import java.util.List;

import com.jukisawa.discordBot.entity.LoLAccount;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class LoLAccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(LoLAccount account) {
        entityManager.persist(account);
    }

    public void deleteByLoLName(Long discordUserId, String name, String tag) {
        String jpql = "SELECT a FROM LoLAccount a WHERE a.discordUserId = :discordUserId AND a.lolGameName = :lolGameName AND a.lolTagLine = :lolTagLine";
        LoLAccount account = entityManager.createQuery(jpql, LoLAccount.class)
                .setParameter("discordUserId", discordUserId)
                .setParameter("lolGameName", name)
                .setParameter("lolTagLine", tag)
                .getSingleResult();
        if (account != null) {
            entityManager.remove(account);
        }
    }

    public List<LoLAccount> findByDiscordUserId(Long discordUserId) {
        return entityManager.createQuery("FROM LoLAccount WHERE discordUserId = :discordUserId", LoLAccount.class)
                .setParameter("discordUserId", discordUserId)
                .getResultList();
    }
}