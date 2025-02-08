CREATE DATABASE TaskSys;
go

USE TaskSys;
go

DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS Task;
DROP TABLE IF EXISTS Conversation;
DROP TABLE IF EXISTS ConversationParticipant;
DROP TABLE IF EXISTS Message;
DROP TABLE IF EXISTS Notification;
DROP TABLE IF EXISTS Eventlog;


CREATE TABLE roles (
   roleId INT NOT NULL PRIMARY KEY,
   roleName NVARCHAR(255) NOT NULL,
   create BIT NOT NULL,
   read BIT NOT NULL,
   delete BIT NOT NULL,
   update BIT NOT NULL
);


CREATE TABLE Users (
   userId BIGINT NOT NULL PRIMARY KEY,
   username NVARCHAR(255) NOT NULL,
   password NVARCHAR(255) NOT NULL,
   roleId INT NOT NULL, 
   isAdmin BIT 
   FOREIGN KEY (roleId) REFERENCES roles(roleId) ON DELETE SET NULL
);

CREATE TABLE user_roles (
    user_id INT NOT NULL,             
    role_id INT NOT NULL,             
    PRIMARY KEY (user_id, role_id),   
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,   
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE    
);



CREATE TABLE Task (
   taskId BIGINT NOT NULL PRIMARY KEY,
   taskTitle NVARCHAR(255) NOT NULL,
   description NVARCHAR(MAX),
   assignedTo BIGINT NULL,
   status NVARCHAR(50),
   deadline DATETIME,
   FOREIGN KEY (assignedTo) REFERENCES Users(userId) ON DELETE SET NULL
);

CREATE TABLE Conversation (
   conversationId BIGINT NOT NULL PRIMARY KEY,
   createdAt DATETIME,
   lastUpdatedAt DATETIME
);

CREATE TABLE ConversationParticipant (
   conversationId BIGINT NOT NULL,
   userId BIGINT NOT NULL,
   PRIMARY KEY (conversationId, userId),
   FOREIGN KEY (conversationId) REFERENCES Conversation(conversationId) ON DELETE CASCADE,
   FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE
);

CREATE TABLE Message (
   messageId BIGINT NOT NULL PRIMARY KEY IDENTITY(1,1),
   conversationId BIGINT NOT NULL,
   userId BIGINT NOT NULL,
   content NVARCHAR(MAX) NOT NULL,
   sendAt DATETIME NOT NULL DEFAULT GETDATE(),
   FOREIGN KEY (conversationId) REFERENCES Conversation(conversationId) ON DELETE CASCADE,
   FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE
);

CREATE TABLE Notification (
   notificationId BIGINT NOT NULL PRIMARY KEY IDENTITY(1,1),
   userId BIGINT NOT NULL,
   content NVARCHAR(MAX) NOT NULL,
   type NVARCHAR(50) NOT NULL,
   readStatus BIT NOT NULL DEFAULT 0,
   notificationTitle NVARCHAR(50) NOT NULL,
   FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE
);

CREATE TABLE Eventlog (
   logId BIGINT NOT NULL PRIMARY KEY IDENTITY(1,1),
   userId BIGINT NULL,
   logContent NVARCHAR(MAX) NOT NULL,
   eventTime DATETIME NOT NULL DEFAULT GETDATE(),
   FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE SET NULL
);
