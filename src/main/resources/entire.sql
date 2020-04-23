CREATE DATABASE walletdb;
USE walletdb;

DROP TABLE IF EXISTS Wallet;
CREATE TABLE `Wallet`
(
    `id`           BIGINT(20)  NOT NULL AUTO_INCREMENT,
    `currencyCode` VARCHAR(3)  NOT NULL,
    `walletType`   VARCHAR(64) NOT NULL,
    `customerId`   BIGINT(20),
    PRIMARY KEY (`id`),
    INDEX INDEX_CUSTOMER_ID (customerId)
);

DROP TABLE IF EXISTS Campaign;
CREATE TABLE `Campaign`
(
    `id`                BIGINT(20)     NOT NULL AUTO_INCREMENT,
    `name`              VARCHAR(255)   NOT NULL,
    `balance`           DECIMAL(13, 2) NOT NULL DEFAULT 0.00,
    `budget`            DECIMAL(13, 2) NOT NULL DEFAULT 0.00,
    `walletId`          BIGINT(20)     NOT NULL,
    `lastTransactionId` BIGINT(20),
    PRIMARY KEY (`id`),
    INDEX INDEX_WALLET_ID (walletId),
    FOREIGN KEY FK_WALLET_ID (`walletId`) REFERENCES Wallet (id)
);

DROP TABLE IF EXISTS Transaction;
CREATE TABLE `Transaction`
(
    `id`                   BIGINT(20)     NOT NULL AUTO_INCREMENT,
    `date`                 TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `originalAmount`       DECIMAL(13, 2) NOT NULL,
    `originalCurrencyCode` VARCHAR(3)     NOT NULL,
    `parentId`             BIGINT(20),
    `operation`            VARCHAR(64)    NOT NULL,
    `notes`                VARCHAR(255),
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS TransactionPart;
CREATE TABLE `TransactionPart`
(
    `id`            BIGINT(20)     NOT NULL AUTO_INCREMENT,
    `transactionId` BIGINT(20)     NOT NULL,
    `walletId`      BIGINT(20)     NOT NULL,
    `direction`     VARCHAR(64)    NOT NULL,
    `amount`        DECIMAL(13, 2) NOT NULL,
    `currencyCode`  VARCHAR(3)     NOT NULL,
    `refund`        TINYINT(1)     NOT NULL DEFAULT FALSE,
    PRIMARY KEY (`id`),
    FOREIGN KEY FK_TRANSACTION_ID (`transactionId`) REFERENCES `Transaction` (id),
    FOREIGN KEY FK_WALLET_ID (`walletId`) REFERENCES Wallet (id),
    INDEX INDEX_WALLET_ID (walletId),
    INDEX INDEX_TRANSACTION_ID (transactionId)
);

# insert data
INSERT INTO `Wallet` (`id`, `currencyCode`, `walletType`)
VALUES (1, 'EUR', 'EXPENSE_WALLET');
