SIMIONESCU ANA-MARIA
323CA

Start Date: 1.11.2019
Finish Date: 10.11.2019

I created the BasePlayer class that is later extended by the Greedy and Bribed
types of players. I implemented here the methods for :
	-drawing cards: same for every type of player; 
	-creating the bag (there is a base method used by
the basic player and one for greedy and bribe that uses at some point the former one)
	-checking the bag of another player
	-the whole implementation of a player's turn as sheriff
	-adding on the stand the goods provided by illegal bonuses
	-getting the number of goods of each type
	-taking bribe

For the Score Board, I created a class ScoreBoardEntry and its comparator

In the Main class, a new object is created for each player and then the logic 
of the game is followed. At the end, the profit for each good on the stand is
added and also the king and queen bonuses for each type of asset.

I had dificulties in understanding the whole concept of the homework and all
the rules of the game. But afterwards the implementation was not that hard.
