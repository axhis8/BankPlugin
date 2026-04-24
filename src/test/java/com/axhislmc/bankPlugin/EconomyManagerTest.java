package com.axhislmc.bankPlugin;

import com.axhislmc.bankPlugin.managers.DatabaseManager;
import com.axhislmc.bankPlugin.managers.EconomyManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EconomyManagerTest {

    @Mock
    private BankPlugin plugin;

    @Mock
    private DatabaseManager dbManager;

    private EconomyManager economyManager;

    @BeforeEach
    void setUp() {
        when(plugin.getDatabaseManager()).thenReturn(dbManager);
        when(dbManager.getBalance(any())).thenReturn(5000.0);
        economyManager = new EconomyManager(plugin);
    }

    @Test
    void getBalanceRandomPlayer() {
        assertEquals(5000.0, economyManager.getBalance(UUID.randomUUID()));
    }

    @Test
    void testCacheLogic() {
        UUID uuid = UUID.randomUUID();

        economyManager.loadPlayer(uuid);

        economyManager.getBalance(uuid);
        economyManager.getBalance(uuid);

        verify(dbManager, times(1)).getBalance(uuid);
    }
}