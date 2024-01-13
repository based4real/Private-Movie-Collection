USE master;
GO

IF EXISTS(SELECT * FROM sys.databases WHERE name = 'PMC')
BEGIN
    ALTER DATABASE PMC SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE PMC;
END
GO

CREATE DATABASE PMC;
GO

USE PMC;
GO

IF OBJECT_ID('dbo.Movie', 'U') IS NOT NULL
    DROP TABLE dbo.Movie;
GO
CREATE TABLE Movie (
    id INT PRIMARY KEY IDENTITY(1,1),
    tmdbId INT,
    imdbId VARCHAR(50),
    title NVARCHAR(255) NOT NULL,
    imdbRating DECIMAL(3, 1),
    personalRating DECIMAL(3, 1),
    filePath NVARCHAR(255),
    posterPath NVARCHAR(255),
    lastSeen DATETIME
);
GO

IF OBJECT_ID('dbo.Genre', 'U') IS NOT NULL
    DROP TABLE dbo.Genre;
GO
CREATE TABLE Genre (
    tmdbId INT PRIMARY KEY
);
GO

IF OBJECT_ID('dbo.MovieGenre', 'U') IS NOT NULL
    DROP TABLE dbo.MovieGenre;
GO
CREATE TABLE MovieGenre (
    MovieId INT,
    GenreId INT,
    PRIMARY KEY (MovieId, GenreId),
    FOREIGN KEY (MovieId) REFERENCES Movie(id),
    FOREIGN KEY (GenreId) REFERENCES Genre(tmdbId)
);
GO

if OBJECT_ID('dbo.Category', 'U') IS NOT NULL
    DROP TABLE dbo.Category;
GO
CREATE TABLE Category (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(80)
);
GO

IF OBJECT_ID('dbo.CatMovie', 'U') IS NOT NULL
    DROP TABLE dbo.CatMovie;
GO
CREATE TABLE CatMovie (
    CategoryId INT,
    MovieId INT,
    PRIMARY KEY (MovieId, CategoryId),
    FOREIGN KEY (MovieId) REFERENCES Movie(id),
    FOREIGN KEY (CategoryId) REFERENCES Category(id)
);
GO

INSERT INTO Movie (tmdbId, imdbId, title, imdbRating, personalRating, filePath, posterPath, lastSeen)
VALUES
(11324, 'tt1130884', 'Shutter Island', 8.2, 7.4, 'Shutter.Island.mp4', '4GDy0PHYX3VRXUtwK5ysFbg3kEx.jpg', '2023-05-02 07:04:00'),
(3176, 'tt0266308', N'バトル・ロワイアル', 7.5, 2.3, 'Battle.Royale.mp4', 'gFX7NuBUeKysOB9nEzRqVpHNT32.jpg', '2017-05-02 17:04:00'),
(580175, 'tt10288566', 'Druk', 7.7, 10.0, 'Another.Round.mp4', 'aDcIt4NHURLKnAEu7gow51Yd00Q.jpg', '2019-12-24 18:00:43'),
(278, 'tt0111161', 'The Shawshank Redemption', 9.3, 0.0, 'The.Shawshank.Redemption.mp4', 'q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg', '2024-01-02 12:03:00'),
(872585, 'tt15398776', 'Oppenheimer', 8.4, 5.9, 'Oppenheimer.mp4', '8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg', '2020-07-23 23:57:19'),
(346698, 'tt1517268', 'Barbie', 7.0, 6.0, 'Barbie.mp4', 'iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg', DATEADD(YEAR, -2, GETDATE())),
(19634, 'tt0045750', 'Far til fire', 6.3, RAND() * 10, 'Father.of.Four.mp4', 'jx7s9x4cob81CaXmFVnU311189n.jpg', DATEADD(DAY, -CAST(RAND()*1000 AS INT), GETDATE())),
(688656, 'tt10360916', 'Richard Sorge - Stalins James Bond', 6.6, RAND() * 10, 'Stalins.James.Bond.mp4', 'j523NJfe7KRUfBLSOPsGxrTgoyR.jpg', DATEADD(DAY, -CAST(RAND()*1000 AS INT), GETDATE())),
(9615, 'tt0463985', 'The Fast and the Furious: Tokyo Drift', 6.0, RAND() * 10, 'The.Fast.and.the.Furious.Tokyo.Drift.mp4', '46xqGOwHbh2TH2avWSw3SMXph4E.jpg', DATEADD(DAY, -CAST(RAND()*1000 AS INT), GETDATE())),
(174958, 'tt0010208', 'Harakiri', 5.6, RAND() * 10, 'Harakiri.mp4', 'xDXMUdERs3uFtgO8P5J5WyWT554.jpg', DATEADD(DAY, -CAST(RAND()*1000 AS INT), GETDATE())),
(47916, 'tt0098214', 'Rojo amanecer', 8.0, RAND() * 10, 'Red.Dawn.mp4', 'iM6w5hUnxUAeRkDC4bYaGmD96nl.jpg', DATEADD(DAY, -CAST(RAND()*1000 AS INT), GETDATE())),
(875188, 'tt15434074', N'खो गए हम कहाँ', 8.4, RAND() * 10, 'Kho.Gaye.Hum.Kahan.mp4', 'WUptEusy1sMh3s46Ik7QbfoKts.jpg', DATEADD(DAY, -CAST(RAND()*1000 AS INT), GETDATE()));

INSERT INTO Genre (tmdbId)
VALUES
(18), (53), (9648), (28), (35), (80), (36), (12), (14), (10751), (99), (10770);

INSERT INTO MovieGenre (MovieId, GenreId)
VALUES
(1, 18), (1, 53), (1, 9648), -- Shutter Island
(2, 18), (2, 53), (2, 28), -- バトル・ロワイアル (Battle Royale)
(3, 35), (3, 18), -- Druk
(4, 18), (4, 80), -- The Shawshank Redemption
(5, 18), (5, 36), -- Oppenheimer
(6, 35), (6, 12), (6, 14), -- Barbie
(7, 10751), (7, 35), -- Far til Fire
(8, 99), (8, 36), (8, 10770), -- Stalin's James Bond
(9, 28), (9, 80), (9, 18), (9, 53), -- The Fast and the Furious: Tokyo Drift
(10, 18), -- Harakiri
(11, 18), (11, 53), (11, 36), -- Rojo amanecer
(12, 18); -- Kho Gaye Hum Kahan (खो गए हम कहाँ);

INSERT INTO Category (name) VALUES ('International'), ('Engelsk'), ('Fast n Furious');

INSERT INTO CatMovie (CategoryId, MovieId)
VALUES
(1, 2), (1, 3), (1, 7), (1, 10), (1, 11), (1, 12), -- International kategori
(2, 1), (2, 4), (2, 5), (2, 6), (2, 8), (2, 9), -- Engelsk kategori
(3, 9); -- Fast n Furious kategori

