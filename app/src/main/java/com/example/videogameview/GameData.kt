package com.example.videogameview

class GameData {
    companion object {
        fun getAll(): List<Game> {
            return listOf(
                Game(
                    "Minecraft",
                    "PC/Xbox/Playstation",
                    "18.11.2011",
                    10.0,
                    "https://fs-prod-cdn.nintendo-europe.com/media/images/10_share_images/games_15/nintendo_switch_4/H2x1_NSwitch_Minecraft_image1600w.jpg",
                    "10+",
                    "Mojang Studios",
                    "Mojang Studios",
                    "Sandbox",
                    "Minecraft is a video game in which players create and break apart various kinds of blocks in three-dimensional worlds. The game's two main modes are Survival and Creative. In Survival, players must find their own building supplies and food. They also interact with blocklike mobs, or moving creatures.",
                    mutableListOf<UserImpression>(
                        UserRating("khalilovic", System.currentTimeMillis(), 5.5),
                        UserReview("edzeko", System.currentTimeMillis(), "Great game!"),
                        UserReview(
                            "ahodzic",
                            System.currentTimeMillis(),
                            "More than a block game."
                        ),
                        UserRating("jsmith", System.currentTimeMillis(), 10.0),
                        UserRating("aarnautovic", System.currentTimeMillis(), 9.5)
                    )
                ),
                Game(
                    "Fortnite",
                    "PC/Xbox/Playstation",
                    "25.07.2017",
                    9.0,
                    "https://d.newsweek.com/en/full/1968454/fortnite-chapter-3-season-1-keyart.jpg",
                    "13+",
                    "Epic Games",
                    "Epic Games",
                    "Battle Royale",
                    "Fortnite is a survival game where 100 players fight against each other in player versus player combat to be the last one standing.",
                    mutableListOf<UserImpression>(
                        UserRating(
                            "Proba",
                            System.currentTimeMillis(),
                            5.5
                        )
                    )
                ),
                Game(
                    "GTA V",
                    "PC/Xbox/Playstation",
                    "17.09.2013",
                    8.0,
                    "https://i.ytimg.com/vi/foUaOCzfIRU/maxresdefault.jpg",
                    "18+",
                    "Rockstar North",
                    "Rockstar Games",
                    "Action",
                    "Grand Theft Auto V is an action-adventure game played from either a third-person or first-person perspective. Players complete missions—linear scenarios with set objectives—to progress through the story. Outside of the missions, players may freely roam the open world.",
                    mutableListOf<UserImpression>()
                ),
                Game(
                    "Overwatch",
                    "PC/Xbox/Playstation",
                    "24.05.2016",
                    7.5,
                    "https://cdn.ndtv.com/tech/gadgets/overwatch_versus_.jpg",
                    "13+",
                    "Blizzard Entertainment",
                    "Blizzard Entertainment",
                    "First-person shooter",
                    "Overwatch is an online team-based game generally played as a first-person shooter. The game features several different game modes, principally designed around squad-based combat with two opposing teams of six players each.",
                    mutableListOf<UserImpression>()
                ),
                Game(
                    "The Legend of Zelda",
                    "Nintendo",
                    "16.07.2021",
                    8.7,
                    "https://www.zelda.com/breath-of-the-wild/assets/icons/BOTW-Share_icon.jpg",
                    "13+",
                    "Nintendo EPD",
                    "Nintendo",
                    "Action-adventure",
                    "The series centers on the various incarnations of Link, a courageous young man of the elf-like Hylian race; and Princess Zelda, a magical princess who is the mortal reincarnation of the goddess Hylia; as they fight to save the magical land of Hyrule from Ganon, an evil warlord turned demon king, who is the principal antagonist of the series.",
                    mutableListOf<UserImpression>()
                ),
                Game(
                    "Roblox",
                    "PC/Xbox/Mobile",
                    "1.09.2006",
                    8.0,
                    "https://www.pockettactics.com/wp-content/sites/pockettactics/2020/10/roblox-logo-e1646306978838.jpg",
                    "10+",
                    "Roblox Corporation",
                    "Roblox Corporation",
                    "Game creation system",
                    "Roblox is a global platform where millions of people gather together every day to imagine, create, and share experiences with each other in immersive, user-generated 3D worlds. The types of gameplay on Roblox are just as limitless as the imagination of the creators themselves.",
                    mutableListOf<UserImpression>()
                ),
                Game(
                    "League of Legends",
                    "PC",
                    "27.10.2009",
                    7.0,
                    "https://images.contentstack.io/v3/assets/blt731acb42bb3d1659/blt670d428d1921eed8/614be30d69b7947c1b3aebd5/9242021_StateofGameplayArticle_Header.jpg",
                    "13+",
                    "Riot Games",
                    "Riot Games",
                    "MOBA",
                    "League of Legends is one of the world's most popular video games, developed by Riot Games. It features a team-based competitive game mode based on strategy and outplaying opponents. Players work with their team to break the enemy Nexus before the enemy team breaks theirs.",
                    mutableListOf<UserImpression>()
                ),
                Game(
                    "Rocket League",
                    "PC/Xbox/Playstation",
                    "07.07.2015",
                    7.6,
                    "https://rocketleague.media.zestyio.com/rl_cross-play_asset_no-text.jpg",
                    "3+",
                    "Psyonix",
                    "Psyonix",
                    "Sports",
                    "Rocket League is a fantastical sport-based video game. It features a competitive game mode based on teamwork and outmaneuvering opponents. Players work with their team to advance the ball down the field, and score goals in their opponents' net.",
                    mutableListOf<UserImpression>()
                ),
                Game(
                    "FIFA 23",
                    "PC/Xbox/Playstation",
                    "30.09.2022",
                    7.0,
                    "https://www.soccerbible.com/media/140938/fifa-10-min.jpg",
                    "3+",
                    "EA Sports",
                    "EA Sports",
                    "Sports",
                    "EA SPORTS™ FIFA 23 brings The World's Game to the pitch, with HyperMotion2 Technology that delivers even more gameplay realism, both the men's and women's FIFA World Cup™ coming to the game as post-launch updates, the addition of women's club teams, cross-play features*, and more.",
                    mutableListOf<UserImpression>()
                ),
                Game(
                    "Gran Turismo 7",
                    "Playstation",
                    "04.03.2022",
                    9.0,
                    "https://outsidergaming.com/wp-content/uploads/2022/03/Gran-Turismo-7-How-to-Take-Photos.jpg",
                    "3+",
                    "Polyphony Digital",
                    "Sony Interactive Entertainment",
                    "Racing simulation",
                    "Gran Turismo 7 shines a spotlight on the long history of cars and their culture as the player embarks on a journey to collect the most historic examples. The game is designed to allow players to learn about the origins of each model and their place in history naturally as the game progresses.",
                    mutableListOf<UserImpression>()
                ),
            )
        }

        fun getDetails(title: String?): Game? {
            var gameList = getAll()
            for (game in gameList) {
                if (game.title == title) {
                    return game
                }
            }
            return null
        }
    }
}