package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void testInvalidPlacement()
    {
        //Initialize the board
        Board board = new Board();          //Player board
        Board board_a = new Board();        //Enemy board

        int i;
            for(i = 1; i <11; i++) {   //row index 1-10
                assertFalse(board.placeShip(new Ship("MINESWEEPER"), i, 'J', false, false));
                assertFalse(board.placeShip(new Ship("BATTLESHIP"), i, 'J', false, false));
                assertFalse(board.placeShip(new Ship("DESTROYER"), i, 'J', false, false));
                assertFalse(board.placeShip(new Ship("SUBMARINE"), i, 'J', false, false));
            }

            for(char j = 'A';j<'K'; j++ ){
                assertFalse(board.placeShip(new Ship("MINESWEEPER"), 10, j, true, false));
                assertFalse(board.placeShip(new Ship("DESTROYER"), 9, j, true, false));
                assertFalse(board.placeShip(new Ship("BATTLESHIP"), 8, j, true, false));
                assertFalse(board.placeShip(new Ship("SUBMARINE"), 8, j, true, false));
            }

            //Valid placement
            assertTrue(board.placeShip(new Ship("MINESWEEPER"), 6, 'A', false, false));
            assertTrue(board.placeShip(new Ship("DESTROYER"), 3, 'C', true, false));
            assertTrue(board.placeShip(new Ship("BATTLESHIP"), 5, 'I', true, false));
            assertTrue(board.placeShip(new Ship("SUBMARINE"), 7, 'E', false, false));

        //Check if the ship could move collapse
        boolean[] result = board_a.move(1);
        assertTrue(result[0] && result[1] && result[2] && result[3]);

        //Check if could move out of board
        result = board_a.move(1);
        assertFalse(result[0] && result[1] && result[2] && result[3]);

        //Check if could move to other direction
        result = board_a.move(3);
        assertTrue(result[0] && result[1] && result[2] && result[3]);

        //Check if ship was initialed correct
        Ship testShip = new Ship();
        assertFalse(testShip.isSunk() == true);

        //Some miss hit result on both board
        assertTrue(board.attack(1,'B').getResult() == AtackStatus.MISS);
        assertTrue(board_a.attack(7,'B').getResult() == AtackStatus.MISS);
        assertTrue(board.attack(7,'I').getResult() == AtackStatus.MISS);
        assertTrue(board_a.attack(5,'I').getResult() == AtackStatus.MISS);
        assertTrue(board.attack(8,'B').getResult() == AtackStatus.MISS);
        assertTrue(board_a.attack(9,'C').getResult() == AtackStatus.MISS);
        assertTrue(board.attack(3,'A').getResult() == AtackStatus.MISS);

        //player hit ship of enemy
        assertTrue(board_a.attack(2,'B').getResult() == AtackStatus.HIT);
        //enemy hit ship of player normally
        assertTrue(board.attack(3,'B').getResult() == AtackStatus.HIT);
        //player sunk ship of enemy
        assertTrue(board_a.attack(1,'B').getResult() == AtackStatus.SUNK);
        //enemy sunk ship of player
        assertTrue(board.attack(2,'B').getResult() == AtackStatus.SUNK);


        //player hit one captain quarter of enemy's ship
        assertTrue(board_a.attack(7,'D').getResult() == AtackStatus.CAPTAIN);
        assertTrue(board_a.attack(6,'D').getResult() == AtackStatus.HIT);
        assertTrue(board_a.attack(5,'D').getResult() == AtackStatus.HIT);
        assertTrue(board_a.attack(4,'D').getResult() == AtackStatus.HIT);
        assertTrue(board.attack(6,'D').getResult() == AtackStatus.CAPTAIN);
        //player sunk one enemy'ship by only hitting twice captain quarter
        assertTrue(board_a.attack(7,'D').getResult() == AtackStatus.SUNK);
        //If player trying to hit the cq more than twice
        assertTrue(board_a.attack(7,'D').getResult() == AtackStatus.HIT);
        //If player trying to hit a square belong to a cq sunk ship
        assertTrue(board_a.attack(6,'D').getResult() == AtackStatus.HIT);
        //enemy sunk one player'ship by only hitting twice captain quarter
        assertTrue(board.attack(6,'D').getResult() == AtackStatus.SUNK);

        //enemy' another captain quarter got hit again
        assertTrue(board_a.attack(7,'H').getResult() == AtackStatus.CAPTAIN);
        assertTrue(board.attack(4,'H').getResult() == AtackStatus.MISS);


        //Hit the battle but can't hit the submarine since
        assertTrue(board_a.attack(7,'H').getResult() == AtackStatus.SUNK);

        //Test the game object
        Game test = new Game();
        assertTrue(test.placeShip(new Ship("MINESWEEPER"), 1, 'D', false, false));
        assertTrue(test.placeShip(new Ship("DESTROYER"), 4, 'D', false, false));
        //Test if could overlap
        assertFalse(test.placeShip(new Ship("BATTLESHIP"), 4, 'D', false, false));
        assertTrue(test.placeShip(new Ship("BATTLESHIP"), 3, 'D', false, false));
        //Test the submerged mode
        assertFalse(test.placeShip(new Ship("SUBMARINE"), 3, 'D', false, false));
        assertTrue(test.placeShip(new Ship("SUBMARINE"), 3, 'D', false, true));

        //The game should have ability to fire
        assertTrue(test.attack(1, 'A'));

        //If player trying to move the fleet before sunk two opponent's ship
        assertFalse(test.moveFleet(1));


    }

}
