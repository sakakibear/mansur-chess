# mansur-chess

## Introduction

A chess game using alpha-beta pruning.
It can play games such as Tic-Tac-Toe with human user.
Othello game implemented in Sep. 2021.
More games are going to be implemented in the future.

## Compile and run

`javac game/tictactoe/TicTacToe.java`

`java game.tictactoe.TicTacToe`

or

`javac game/othello/Othello.java`

`java game.othello.Othello`

## Usage

`TicTacToe [-depth n] [-md]`

or

`Othello [-depth n] [-md]`

### Options

`-depth`

The searching depth of game tree. Should be followed by a positive number. The larger the number is, more steps will be searched. It is set to 5 by default.

`-md`

Move defensive (human player). AI will move offensive (first) is this option is set.

## About Mansur

"Mansur" means "bear" in Altai language.

It is also the name of an orphaned bear adopted by a pilot living near Moscow.

This project is named "Mansur" to express author's affection for bears and Mansur.

To know more about the bear Mansur, please see following links.

https://www.mansur.il-14.ru/?lang=en

https://www.youtube.com/channel/UCBx231jNeUk4cUXc4lBdYpQ