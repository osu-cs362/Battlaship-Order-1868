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
      
        //enemy board error setting --------------------------------------------------------------------
        assertFalse(board_a.placeShip(new Ship("MINESWEEPER"), 10, 'J', true, false));     //To check if the place ship will throw error if in invalid position
        assertFalse(board_a.placeShip(new Ship("BATTLESHIP"), 10, 'J', true, false));
        assertFalse(board_a.placeShip(new Ship("DESTROYER"), 10, 'J', true, false));
        assertFalse(board_a.placeShip(new Ship("MINESWEEPER"), 0, 'J', false, false));
        assertFalse(board_a.placeShip(new Ship("BATTLESHIP"), 0, 'J', false, false));
        assertFalse(board_a.placeShip(new Ship("DESTROYER"), 0, 'I', false, false));
        assertFalse(board_a.placeShip(new Ship("SUBMARINE"), 0, 'J', true, true));
        //---------------------------------------------------------------------------------------------

        //enemy board correct setting------------------------------------------------------------------
        assertTrue(board_a.placeShip(new Ship("MINESWEEPER"), 2, 'B', true, false));       //To check if the place ship will correct insert the ship
        //error becuase square overlap
        assertFalse(board_a.placeShip(new Ship("BATTLESHIP"), 2, 'B', false, false));
        assertTrue(board_a.placeShip(new Ship("BATTLESHIP"), 6, 'D', true, false));
        //error becasue place same ship again
        assertFalse(board_a.placeShip(new Ship("MINESWEEPER"), 7, 'H', false, false));
        assertTrue(board_a.placeShip(new Ship("DESTROYER"), 7, 'H', true, false));

        //cant place same spot with Destroyer because submarine is not submerged
        assertFalse(board_a.placeShip(new Ship("SUBMARINE"), 7, 'H', true, false));
        //can place submarine because submerged
        assertTrue(board_a.placeShip(new Ship("SUBMARINE"), 7, 'H', true, true));

        //--------------------------------------------------------------------------------------------


        //player board error setting----------------------------------------------------------------------
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 10, 'J', true, false));     //To check if the place ship will throw error if in invalid position
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), 10, 'J', true, false));
        assertFalse(board.placeShip(new Ship("DESTROYER"), 10, 'J', true, false));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 0, 'J', false, false));
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), 0, 'J', false, false));
        assertFalse(board.placeShip(new Ship("DESTROYER"), 0, 'I', false, false));
        //------------------------------------------------------------------------------------------------

        //player board correct setting -------------------------------------------------------------------
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 3, 'B', true, false));       //To check if the place ship will correct insert the ship
        //error becuase square overlap
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), 3, 'B', false, false));
        assertTrue(board.placeShip(new Ship("BATTLESHIP"), 5, 'D', true, false));
        //error becasue place same ship again
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 7, 'H', false, false));
        assertTrue(board.placeShip(new Ship("DESTROYER"), 4, 'H', false, false));
        //----------------------------------------------------------------------------------------------

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
        /*
            assertTrue(board.placeShip(new Ship("MINESWEEPER"), 6, 'A', false, false));
            assertTrue(board.placeShip(new Ship("DESTROYER"), 3, 'C', true, false));
            assertTrue(board.placeShip(new Ship("BATTLESHIP"), 5, 'I', true, false));
            assertTrue(board.placeShip(new Ship("SUBMARINE"), 7, 'E', false, false));
        */

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

        //The game should have ability to fire, and the coordinate should be valid
        for(i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                assertTrue(test.attack(i+1, (char)(65+j)));
            }
        }
        assertFalse(test.attack(-1, 'A'));
        assertFalse(test.attack(0, 'A'));
        assertFalse(test.attack(11, 'J'));
        assertFalse(test.attack(11, 'K'));

        //The game should have ability to fire
        assertTrue(test.attack(1, 'A'));

        //If player trying to move the fleet before sunk two opponent's ship
        assertFalse(test.moveFleet(1));

        //Test the ship
        Ship min = new Ship("MINESWEEPER");
        Ship des = new Ship("DESTROYER");
        Ship bat = new Ship("BATTLESHIP");
        Ship sub = new Ship("SUBMARINE");

        //Test the allocate
        assertTrue(min.returnHp() == 2);
        assertTrue(des.returnHp() == 3);
        assertTrue(bat.returnHp() == 4);
        assertTrue(sub.returnHp() == 5);

        assertTrue(min.returnCHp() == 1);
        assertTrue(des.returnCHp() == 2);
        assertTrue(bat.returnCHp() == 2);
        assertTrue(sub.returnCHp() == 2);

        assertTrue(min.isSunk() == false);
        assertTrue(des.isSunk() == false);
        assertTrue(bat.isSunk() == false);
        assertTrue(sub.isSunk() == false);

        //Test place ship
        bat.setCoordinates(1,'A', false, false);

        assertTrue(bat.getOccupiedSquares().get(0).getRow() == 1 && bat.getOccupiedSquares().get(0).getColumn() == 'A');
        assertTrue(bat.isSubmerged() == false);
        assertTrue(bat.getOccupiedSquares().get(3).getRow() == 1 && bat.getOccupiedSquares().get(3).getColumn() == 'D');

        //Test move the ship
        bat.move(1);
        assertTrue(bat.getOccupiedSquares().get(0).getRow() == 1 && bat.getOccupiedSquares().get(0).getColumn() == 'A');
        assertTrue(bat.isSubmerged() == false);
        assertTrue(bat.getOccupiedSquares().get(3).getRow() == 1 && bat.getOccupiedSquares().get(3).getColumn() == 'D');

        bat.move(3);
        assertTrue(bat.getOccupiedSquares().get(0).getRow() == 2 && bat.getOccupiedSquares().get(0).getColumn() == 'A');
        assertTrue(bat.isSubmerged() == false);
        assertTrue(bat.getOccupiedSquares().get(3).getRow() == 2 && bat.getOccupiedSquares().get(3).getColumn() == 'D');

        bat.move(2);
        assertTrue(bat.getOccupiedSquares().get(0).getRow() == 2 && bat.getOccupiedSquares().get(0).getColumn() == 'B');
        assertTrue(bat.isSubmerged() == false);
        assertTrue(bat.getOccupiedSquares().get(3).getRow() == 2 && bat.getOccupiedSquares().get(3).getColumn() == 'E');

        bat.move(4);
        assertTrue(bat.getOccupiedSquares().get(0).getRow() == 2 && bat.getOccupiedSquares().get(0).getColumn() == 'A');
        assertTrue(bat.isSubmerged() == false);
        assertTrue(bat.getOccupiedSquares().get(3).getRow() == 2 && bat.getOccupiedSquares().get(3).getColumn() == 'D');

        bat.move(1);
        assertTrue(bat.getOccupiedSquares().get(0).getRow() == 1 && bat.getOccupiedSquares().get(0).getColumn() == 'A');
        assertTrue(bat.isSubmerged() == false);
        assertTrue(bat.getOccupiedSquares().get(3).getRow() == 1 && bat.getOccupiedSquares().get(3).getColumn() == 'D');

        bat.shipSunk();
        assertTrue(bat.isSunk());
    }

}
