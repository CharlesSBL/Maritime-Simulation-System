package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * App
 */
public class App {
    public static void main(String[] args) {
        new GenWorld(args).run();
    }
}

/**
 * GenWorld
 */
class GenWorld implements Runnable {
    private final String[] args;

    public GenWorld(String[] args) {
        this.args = args;
    }

    @Override
    public void run() {
        System.out.println("Hello world!");

        Random random = new Random();
        System.out.println("Random object generated");

        Location initialLocation = new Location(0, 0);
        System.out.println("Tunnel generated");
        Tunnel tunnel = new Tunnel(initialLocation);

        System.out.println("Creating PortDataBase");
        PortDataBase portDataBase = new FabricPortDataBase().create();

        System.out.println("Creating FabricShipStorage");
        FabricShipStorage fabricShipStorage = new FabricShipStorage(
                ShipStorageCapacity.SMALL, ShipStorageCapacity.MIDDLE,
                ShipStorageCapacity.BIG);

        System.out.println("Creating FabricPort");
        FabricPort fabricPort = new FabricPort(random, portDataBase, new MultiFabricProduct(), fabricShipStorage);

        int numPortsToAdd = 4;
        for (int i = 0; i < numPortsToAdd; i++) {
            System.out.println("Adding a new Port to PortDataBase");
            portDataBase.getPorts().add(fabricPort.create(150));
        }

        for (Port port : portDataBase.getPorts()) {
            System.out.println("Port: " + port.getID() + " is active...");
            new Thread(new ActivePort(port, random, tunnel)).start();
        }
    }
}

/**
 * MAIN OBJ
 * ========================================================================================================
 * ========================================================================================================
 */

/**
 * PortDataBase
 */
class PortDataBase {
    private final List<Port> ports;

    public PortDataBase(List<Port> ports) {
        /**
         * Initialize a PortDataBase with a list of ports.
         *
         * @param ports: The list of ports to store in the database.
         */
        this.ports = ports;
    }

    public List<Port> getPorts() {
        /**
         * Get the list of ports stored in the database.
         *
         * @return List<Port>: The list of ports.
         */
        return ports;
    }
}

/**
 * Ship
 */
class Ship implements ObjLocation {
    private final int ID;
    private List<ShipElement> elements;
    private Location location;
    private String nameOfCreator;

    public Ship(int id, List<ShipElement> elements, Location location) {
        /**
         * Initialize a Ship with an ID, elements, and location.
         *
         * @param id:       The ID of the ship.
         * @param elements: List of ship elements.
         * @param location: The location of the ship.
         */
        this.elements = elements;
        this.location = location;
        this.ID = id;
    }

    public List<ShipElement> getElements() {
        /**
         * Get the list of ship elements.
         *
         * @return List<ShipElement>: List of ship elements.
         */
        return elements;
    }

    @Override
    public Location getLocation() {
        /**
         * Get the location of the ship.
         *
         * @return Location: The location of the ship.
         */
        return this.location;
    }

    public void setNameOfCreator(String nameOfCreator) {
        /**
         * Set the name of the ship's creator.
         *
         * @param nameOfCreator: The name of the ship's creator.
         */
        this.nameOfCreator = nameOfCreator;
    }

    public String getNameOfCreator() {
        /**
         * Get the name of the ship's creator.
         *
         * @return String: The name of the ship's creator.
         */
        return nameOfCreator;
    }

    public int getID() {
        /**
         * Get the ID of the ship.
         *
         * @return int: The ID of the ship.
         */
        return ID;
    }
}

/**
 * Port
 */
class Port implements ObjLocation {
    private final PortDataBase portDataBase;
    private final int ID;
    private final Location location;
    private final FabricShip fabricShip;
    private final Product product;
    private ShipStorage shipStorage;

    public Port(PortDataBase portDataBase,
            int id,
            Location location,
            FabricShip fabricShip,
            Product product,
            ShipStorage shipStorage) {
        /**
         * Initialize a Port with the specified attributes.
         *
         * @param portDataBase: The database associated with the port.
         * @param id:           The ID of the port.
         * @param location:     The location of the port.
         * @param fabricShip:   The fabric to create ships.
         * @param product:      The product available at the port.
         * @param shipStorage:  The storage for ships at the port.
         */
        this.portDataBase = portDataBase;
        this.ID = id;
        this.location = location;
        this.fabricShip = fabricShip;
        this.product = product;
        this.shipStorage = shipStorage;
    }

    public void setShipStorage(ShipStorage shipStorage) {
        this.shipStorage = shipStorage;
    }

    public Product getProduct() {
        /**
         * Get the product available at the port.
         *
         * @return Product: The product at the port.
         */
        return product;
    }

    public FabricShip getFabricShip() {
        /**
         * Get the fabric for creating ships.
         *
         * @return FabricShip: The fabric ship associated with the port.
         */
        return fabricShip;
    }

    public ShipStorage getShipStorage() {
        /**
         * Get the storage for ships at the port.
         *
         * @return ShipStorage: The ship storage at the port.
         */
        return shipStorage;
    }

    public PortDataBase getPortDataBase() {
        /**
         * Get the database associated with the port.
         *
         * @return PortDataBase: The database of the port.
         */
        return portDataBase;
    }

    public int getID() {
        /**
         * Get the ID of the port.
         *
         * @return int: The ID of the port.
         */
        return ID;
    }

    @Override
    public Location getLocation() {
        /**
         * Get the location of the port.
         *
         * @return Location: The location of the port.
         */
        return this.location;
    }
}

/**
 * Tunel
 */
class Tunnel implements ObjLocation {
    private static final int MAX_CONNECTIONS = 5;
    private final Semaphore semaphore = new Semaphore(MAX_CONNECTIONS);
    private Location location;

    public Tunnel(Location location) {
        /**
         * Initialize a Tunnel with a specified location.
         *
         * @param location: The location of the tunnel.
         */
        this.location = location;
    }

    public Semaphore getSemaphore() {
        /**
         * Get the semaphore associated with the tunnel.
         *
         * @return Semaphore: The semaphore for managing connections.
         */
        return semaphore;
    }

    @Override
    public Location getLocation() {
        /**
         * Get the location of the tunnel.
         *
         * @return Location: The location of the tunnel.
         */
        return this.location;
    }
}

/**
 * LOCATION
 * ========================================================================================================
 * ========================================================================================================
 */

/**
 * ObjLocation
 */
interface ObjLocation {
    /**
     * Get the location of an object.
     *
     * @return Location: The location of the object.
     */
    Location getLocation();
}

/**
 * Location
 */
class Location {
    private int x;
    private int y;

    public Location(int x, int y) {
        /**
         * Initialize a Location with x and y coordinates.
         *
         * @param x: The x-coordinate.
         * @param y: The y-coordinate.
         */
        this.x = x;
        this.y = y;
    }

    public int getX() {
        /**
         * Get the x-coordinate of the location.
         *
         * @return int: The x-coordinate.
         */
        return x;
    }

    public int getY() {
        /**
         * Get the y-coordinate of the location.
         *
         * @return int: The y-coordinate.
         */
        return y;
    }

    public void setX(int x) {
        /**
         * Set the x-coordinate of the location.
         *
         * @param x: The new x-coordinate.
         */
        this.x = x;
    }

    public void setY(int y) {
        /**
         * Set the y-coordinate of the location.
         *
         * @param y: The new y-coordinate.
         */
        this.y = y;
    }
}

/**
 * PORT ELEMENTS
 * ========================================================================================================
 * ========================================================================================================
 */

/**
 * PortElement<T>
 */
interface PortElement<T> extends ShipElement<T> {
    /**
     * This interface extends ShipElement and represents elements related to a port.
     *
     * @param <T>: The type of element.
     */
}

/**
 * ShipStorageCapacity
 */
enum ShipStorageCapacity implements PortElement<Integer> {
    BIG(8),
    MIDDLE(4),
    SMALL(2);

    private final int value;

    ShipStorageCapacity(int value) {
        /**
         * Initialize a ShipStorageCapacity enum value with a specified capacity value.
         *
         * @param value: The capacity value for the enum.
         */
        this.value = value;
    }

    @Override
    public Integer get() {
        /**
         * Get the capacity value associated with the enum.
         *
         * @return int: The capacity value.
         */
        return this.value;
    }
}

/**
 * ShipStorage
 */
class ShipStorage implements PortElement<List<Ship[]>> {
    private final Ship[] big;
    private final Ship[] middle;
    private final Ship[] small;

    public ShipStorage(ShipStorageCapacity big, ShipStorageCapacity middle, ShipStorageCapacity small) {
        /**
         * Initialize a ShipStorage with different capacities for big, middle, and small
         * ships.
         *
         * @param big:    Capacity for big ships.
         * @param middle: Capacity for middle-sized ships.
         * @param small:  Capacity for small ships.
         */
        this.big = new Ship[big.get()];
        this.middle = new Ship[middle.get()];
        this.small = new Ship[small.get()];
    }

    public ShipStorage(List<Ship[]> ships) {
        this.big = ships.get(0);
        this.middle = ships.get(1);
        this.small = ships.get(2);
    }

    @Override
    public List<Ship[]> get() {
        /**
         * Get a list of ship arrays containing big, middle, and small ships.
         *
         * @return List<Ship[]>: A list of ship arrays.
         */
        List<Ship[]> list = new ArrayList<>();
        Collections.addAll(list, big, middle, small);
        return list;
    }

    public Ship[] getBig() {
        /**
         * Get the array of big ships.
         *
         * @return Ship[]: The array of big ships.
         */
        return big;
    }

    public Ship[] getMiddle() {
        /**
         * Get the array of middle-sized ships.
         *
         * @return Ship[]: The array of middle-sized ships.
         */
        return middle;
    }

    public Ship[] getSmall() {
        /**
         * Get the array of small ships.
         *
         * @return Ship[]: The array of small ships.
         */
        return small;
    }
}

/**
 * PortShipCapacity
 */
enum PortShipCapacity implements PortElement<Integer> {
    SMALL(4),
    MIDDLE(16),
    BIG(32);

    private final int value;

    PortShipCapacity(int value) {
        /**
         * Initialize a PortShipCapacity enum value with a specified capacity value.
         *
         * @param value: The capacity value for the enum.
         */
        this.value = value;
    }

    @Override
    public Integer get() {
        /**
         * Get the capacity value associated with the enum.
         *
         * @return Integer: The capacity value.
         */
        return this.value;
    }
}

/**
 * PortProductCapacity
 */
enum PortProductCapacity implements PortElement<Integer> {
    SMALL(ShipCapacity.SMALL.get() * 4),
    MIDDLE(ShipCapacity.MIDDLE.get() * 4),
    BIG(ShipCapacity.BIG.get() * 4);

    private final int value;

    PortProductCapacity(int value) {
        /**
         * Initialize a PortProductCapacity enum value with a specified capacity value.
         *
         * @param value: The capacity value for the enum.
         */
        this.value = value;
    }

    @Override
    public Integer get() {
        /**
         * Get the capacity value associated with the enum.
         *
         * @return Integer: The capacity value.
         */
        return this.value;
    }
}

/**
 * SHIP ELEMENTS
 * ========================================================================================================
 * ========================================================================================================
 */

/**
 * ShipElement
 */
interface ShipElement<T> {
    T get();
}

/**
 * ShipCapacity
 */
enum ShipCapacity implements ShipElement<Integer> {
    SMALL(90),
    MIDDLE(180),
    BIG(360);

    private final int value;

    ShipCapacity(int value) {
        /**
         * Initialize a ShipCapacity enum value with a specified capacity value.
         *
         * @param value: The capacity value for the enum.
         */
        this.value = value;
    }

    @Override
    public Integer get() {
        /**
         * Get the capacity value associated with the enum.
         *
         * @return Integer: The capacity value.
         */
        return this.value;
    }
}

/**
 * PRODUCTS
 * ========================================================================================================
 * ========================================================================================================
 */

/**
 * Product
 */
interface Product<T> extends ShipElement<T> {
    T get();

    void set(T nValue);
}

/**
 * Wood
 */
class Wood implements Product<Integer> {
    private Integer value;

    public Wood(int value) {
        this.value = value;
    }

    @Override
    public Integer get() {
        return this.value;
    }

    @Override
    public void set(Integer nValue) {
        this.value = nValue;

    }
}

/**
 * Meat
 */
class Meat implements Product<Integer> {
    private Integer value;

    public Meat(int value) {
        this.value = value;
    }

    @Override
    public Integer get() {
        return this.value;
    }

    @Override
    public void set(Integer nValue) {
        this.value = nValue;

    }
}

/**
 * Gold
 */
class Gold implements Product<Integer> {
    private Integer value;

    public Gold(int value) {
        this.value = value;
    }

    @Override
    public Integer get() {
        return this.value;
    }

    @Override
    public void set(Integer nValue) {
        this.value = nValue;

    }
}

/**
 * FABRICS
 * ========================================================================================================
 * ========================================================================================================
 */

/**
 * Fabric
 */
interface Fabric<T> {
    T create();

    T create(int val);
}

/**
 * FabricShipStorage
 */
class FabricShipStorage implements Fabric<ShipStorage> {
    private ShipStorageCapacity big;
    private ShipStorageCapacity middle;
    private ShipStorageCapacity small;

    public FabricShipStorage(ShipStorageCapacity big, ShipStorageCapacity middle, ShipStorageCapacity small) {
        this.big = big;
        this.middle = middle;
        this.small = small;
    }

    @Override
    public ShipStorage create() {
        return new ShipStorage(this.big, this.middle, this.small);
    }

    @Override
    public ShipStorage create(int val) {
        // TODO Auto-generated method stub
        return null;
    }
}

/**
 * FabricPortDataBase
 */
class FabricPortDataBase implements Fabric<PortDataBase> {
    @Override
    public PortDataBase create() {
        return new PortDataBase(new ArrayList<>());
    }

    @Override
    public PortDataBase create(int val) {
        // TODO Auto-generated method stub
        return null;
    }
}

/**
 * FabricTunel
 */
class FabricTunel implements Fabric<Tunnel> {
    private Random random;

    public FabricTunel(Random random) {
        this.random = random;
    }

    @Override
    public Tunnel create() {
        return new Tunnel(new Location(this.random.nextInt(10), this.random.nextInt(10)));
    }

    public Tunnel create(Location location) {
        return new Tunnel(location);
    }

    @Override
    public Tunnel create(int val) {
        // TODO Auto-generated method stub
        return null;
    }
}

/**
 * FabricPort
 * Creates a new Port with random products and location.
 */
class FabricPort implements Fabric<Port> {
    private int idCount;
    private final Random random;
    private final PortDataBase portDataBase;
    private final MultiFabricProduct multiFabricProduct;
    private final FabricShipStorage fabricShipStorage;

    public FabricPort(Random random, PortDataBase portDataBase,
            MultiFabricProduct multiFabricProduct, FabricShipStorage fabricShipStorage) {
        this.random = random;
        this.portDataBase = portDataBase;
        this.multiFabricProduct = multiFabricProduct;
        this.fabricShipStorage = fabricShipStorage;
        this.idCount = 0;
    }

    @Override
    public Port create() {
        this.idCount++;
        List<Product> products = this.multiFabricProduct.create();
        int prodLength = products.size();

        return new Port(this.portDataBase, this.idCount,
                new Location(this.random.nextInt(100), this.random.nextInt(100)),
                new FabricShip("Port: " + this.idCount),
                products.get(this.random.nextInt(prodLength)),
                this.fabricShipStorage.create());
    }

    @Override
    public Port create(int val) {
        this.idCount++;
        List<Product> products = this.multiFabricProduct.create(val);
        int prodLength = products.size();

        return new Port(this.portDataBase, this.idCount,
                new Location(this.random.nextInt(100), this.random.nextInt(100)),
                new FabricShip("Port: " + this.idCount),
                products.get(this.random.nextInt(prodLength)),
                this.fabricShipStorage.create());
    }

    public Port create(Location location) {
        this.idCount++;
        List<Product> products = this.multiFabricProduct.create();
        int prodLength = products.size();

        return new Port(this.portDataBase, this.idCount, location,
                new FabricShip("Port: " + this.idCount),
                products.get(this.random.nextInt(prodLength)),
                this.fabricShipStorage.create());
    }

    public void setIdCount(int idCount) {
        this.idCount = idCount;
    }

    public int getIdCount() {
        return idCount;
    }

}

/**
 * FabricShip
 * Creates a new ship with random products and capacities.
 */
class FabricShip implements Fabric<Ship> {
    private static int shipIdCounter = 0;
    private final Random random = new Random();
    private final MultiFabricProduct multiFabricProduct = new MultiFabricProduct();
    private final MultiFabricShipCapacity multiFabricShipCapacity = new MultiFabricShipCapacity();
    private final ShipCapacityGetter shipCapacityGetter = new ShipCapacityGetter();
    private final ShipProductGetter shipProductGetter = new ShipProductGetter();
    private String nameOfCreator;

    public FabricShip() {
        this.genFabric();
    }

    public FabricShip(String nameOfCreator) {
        this.nameOfCreator = nameOfCreator;
        this.genFabric();
    }

    private void genFabric() {
        shipIdCounter = 0;
    }

    private List<ShipElement> prepareElements() {
        List<ShipElement> shipElements = new ArrayList<>(2);

        List<Product> products = multiFabricProduct.create();
        int randomProduct = random.nextInt(products.size());
        shipElements.add(products.get(randomProduct));

        List<ShipCapacity> shipCapacities = multiFabricShipCapacity.create();
        int randomCapacity = random.nextInt(shipCapacities.size());
        shipElements.add(shipCapacities.get(randomCapacity));

        return shipElements;
    }

    private void prepareShip(Ship ship) {
        ShipCapacity shipCapacity = shipCapacityGetter.get(ship);
        shipProductGetter.get(ship).set(shipCapacity.get());
    }

    private int addID() {
        return ++shipIdCounter;
    }

    @Override
    public Ship create() {
        Location location = new Location(0, 0);
        return create(location, nameOfCreator);
    }

    @Override
    public Ship create(int val) {
        // TODO Auto-generated method stub
        return null;
    }

    public Ship create(Location location) {
        return create(location, nameOfCreator);
    }

    public Ship create(Location location, String nameOfCreator) {
        Ship ship = new Ship(addID(), prepareElements(), location);
        prepareShip(ship);
        ship.setNameOfCreator(nameOfCreator);
        return ship;
    }
}

/**
 * FabricProduct
 */
enum FabricProduct implements Fabric<Product> {
    MEAT_FABRIC((Fabric) new FabricMeat()),
    GOLD_FABRIC((Fabric) new FabricGold()),
    WOOD_FABRIC((Fabric) new FabricWood());

    private final Fabric<Product> fabric;

    FabricProduct(Fabric<Product> fabric) {
        this.fabric = fabric;
    }

    @Override
    public Product<Product> create() {
        return fabric.create();
    }

    @Override
    public Product<Product> create(int val) {
        return fabric.create(val);
    }
}

/**
 * FabricMeat
 */
class FabricMeat implements Fabric<Meat> {
    @Override
    public Meat create() {
        return new Meat(0);
    }

    @Override
    public Meat create(int val) {
        return new Meat(val);
    }
}

/**
 * FabricGold
 */
class FabricGold implements Fabric<Gold> {
    @Override
    public Gold create() {
        return new Gold(0);
    }

    @Override
    public Gold create(int val) {
        return new Gold(val);
    }

}

/**
 * FabricWood
 */
class FabricWood implements Fabric<Wood> {
    @Override
    public Wood create() {
        return new Wood(0);
    }

    @Override
    public Wood create(int val) {
        return new Wood(val);
    }
}

/**
 * MULTI_FABRICS
 * ========================================================================================================
 * ========================================================================================================
 */

/**
 * MultiFabric
 */
interface MultiFabric<T> extends Fabric<List<T>> {
    List<T> create();
}

/**
 * MultiFabricShipCapacity
 */
class MultiFabricShipCapacity implements MultiFabric<ShipCapacity> {
    @Override
    public List<ShipCapacity> create() {
        return Arrays.asList(ShipCapacity.values());
    }

    @Override
    public List<ShipCapacity> create(int val) {
        // TODO Auto-generated method stub
        return null;
    }
}

/**
 * MultiFabricProduct
 */
class MultiFabricProduct implements MultiFabric<Product> {
    @Override
    public List<Product> create() {
        List<Product> products = new ArrayList<>(3);

        for (FabricProduct fabrics : FabricProduct.values())
            products.add(fabrics.create());

        return products;
    }

    @Override
    public List<Product> create(int val) {
        List<Product> products = new ArrayList<>(3);

        for (FabricProduct fabrics : FabricProduct.values())
            products.add(fabrics.create(val));

        return products;
    }
}

/**
 * GETTERS
 * ========================================================================================================
 * ========================================================================================================
 */

/**
 * Getter
 */
interface Getter<T> {
    T get(Ship ship);
}

/**
 * ShipCapacityGetter
 * Retrieves the ship capacity element from a ship.
 */
class ShipCapacityGetter implements Getter<ShipCapacity> {
    @Override
    public ShipCapacity get(Ship ship) {
        for (ShipElement element : ship.getElements()) {
            if (element instanceof ShipCapacity) {
                return (ShipCapacity) element;
            }
        }
        return null;
    }
}

/**
 * ShipProductGetter
 * Retrieves the product element from a ship.
 */
class ShipProductGetter implements Getter<Product> {
    @Override
    public Product get(Ship ship) {
        for (ShipElement element : ship.getElements()) {
            if (element instanceof Product) {
                return (Product) element;
            }
        }
        return null;
    }
}

/**
 * SHIP LOGIC
 * ========================================================================================================
 * ========================================================================================================
 */

/**
 * Convertor
 */
class Converter {
    public static Ship objLocationToShip(ObjLocation objLocation) {
        return (objLocation instanceof Ship) ? (Ship) objLocation : null;
    }
}

/**
 * ShipRoad
 * Represents a road for ships to travel between two locations through a tunnel.
 */
class ShipRoad implements Runnable {
    private final Tunnel tunnel;
    private final ObjLocation firstObj;
    private final ObjLocation secondObj;
    private final Random random;

    public ShipRoad(Tunnel tunnel, ObjLocation firstObj, ObjLocation secondObj, Random random) {
        this.tunnel = tunnel;
        this.firstObj = firstObj;
        this.secondObj = secondObj;
        this.random = random;
    }

    @Override
    public void run() {
        try {
            ShipEnterTunnel shipEnterTunnel = new ShipEnterTunnel(this.tunnel, this.firstObj, this.random);
            Thread tunnelThread = new Thread(shipEnterTunnel);
            tunnelThread.start();
            tunnelThread.join(); // Wait for the tunnel thread to finish

            ShipMovement shipMovement = new ShipMovement(firstObj, secondObj);
            shipMovement.run();
        } catch (InterruptedException e) {
            System.out.println("ShipRoad interrupted...");
            Thread.currentThread().interrupt();
        }
    }
}

/**
 * ShipEnterTunnel
 * Represents a ship entering a tunnel.
 */
class ShipEnterTunnel implements Runnable {
    private final Tunnel tunnel;
    private final Ship ship;
    private final Random random;

    public ShipEnterTunnel(Tunnel tunnel, ObjLocation objLocation, Random random) {
        this.tunnel = tunnel;
        this.ship = Converter.objLocationToShip(objLocation);
        this.random = random;
    }

    @Override
    public void run() {
        try {
            System.out.println("Ship: " + ship.getID() + " start wait for enter the tunnel");
            // Attempt to acquire a permit to enter the tunnel
            this.tunnel.getSemaphore().acquire();

            // Calculate the time the ship will spend in the tunnel
            final int timeInTunnel = random.nextInt(8000) + 2000;

            // Check if the ship object is not null before accessing its methods
            if (ship != null) {
                // Log the entry of the ship into the tunnel
                System.out.println(
                        "Ship: " + ship.getID() + " is entering the tunnel for: " + timeInTunnel / 1000 + " seconds");

                // Simulate the time spent in the tunnel
                Thread.sleep(timeInTunnel);

                // Log the exit of the ship from the tunnel
                System.out.println("Ship: " + ship.getID() + " has exited the tunnel.");
            } else {
                // Handle the case when ship is null
                System.out.println("Attempted to enter the tunnel with a null ship reference.");
            }
        } catch (InterruptedException e) {
            // Log interruption details and re-interrupt the thread
            System.out.println("Ship: " + (ship != null ? ship.getID() : "unknown")
                    + " was interrupted while entering or inside the tunnel.");
            Thread.currentThread().interrupt();
        } finally {
            // Ensure that the semaphore permit is released
            this.tunnel.getSemaphore().release();
        }
    }
}

/**
 * ShipMovement
 * Represents the movement of a ship from one location to another.
 */
class ShipMovement implements Runnable {

    private final ObjLocation firstObj;
    private final ObjLocation secondObj;

    public ShipMovement(ObjLocation firstObj, ObjLocation secondObj) {
        this.firstObj = firstObj;
        this.secondObj = secondObj;
    }

    @Override
    public void run() {
        moveCoordinate("X", firstObj.getLocation().getX(), secondObj.getLocation().getX());
        moveCoordinate("Y", firstObj.getLocation().getY(), secondObj.getLocation().getY());
    }

    private void moveCoordinate(String axis, int current, int target) {
        Ship ship = Converter.objLocationToShip(firstObj);
        String shipName = "Ship: " + ship.getID();
        String shipPositionX = "X:" + firstObj.getLocation().getX();
        String shipPositionY = "Y:" + firstObj.getLocation().getY();
        String destinationX = "X:" + secondObj.getLocation().getX();
        String destinationY = "Y:" + secondObj.getLocation().getY();

        System.out.println(shipName + " begins the journey!");
        StringBuilder journeyInfo = new StringBuilder();
        journeyInfo.append("\n== == == == == == == ==\n");
        journeyInfo.append(shipName + " position: " + shipPositionX + " " + shipPositionY + "\n");
        journeyInfo.append("Destination position: " + destinationX + " " + destinationY + "\n");

        if (secondObj instanceof Port) {
            Port port = (Port) secondObj;
            journeyInfo.append(shipName + " begins the journey to Port: " + port.getID() + "!");
        } else if (secondObj instanceof Ship) {
            Ship ship2 = (Ship) secondObj;
            journeyInfo.append(shipName + " begins the journey to ship: " + ship2.getID() + "!");
        }

        final int direction = Integer.compare(target, current);
        int size = Math.abs(target - current);

        while (size > 0) {
            try {
                if (axis.equals("X")) {
                    firstObj.getLocation().setX(current + direction);
                } else if (axis.equals("Y")) {
                    firstObj.getLocation().setY(current + direction);
                }

                size--;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Ship movement interrupted...");
                Thread.currentThread().interrupt();
            }
        }

        journeyInfo.append("\n" + shipName + " reached the point of the route!");
        journeyInfo.append("\n== == == == == == == ==\n");
        System.out.println(journeyInfo.toString());
    }
}

/**
 * PORT LOGIC
 * ========================================================================================================
 * ========================================================================================================
 */
/**
 * ActivePort
 */
class ActivePort implements Runnable {
    private static final int SLEEP_DURATION_MS = 2000;

    private final Port port;
    private final Random random;
    private final Tunnel tunnel;

    public ActivePort(Port port, Random random, Tunnel tunnel) {
        this.port = port;
        this.random = random;
        this.tunnel = tunnel;
    }

    @Override
    public void run() {
        final String portName = "Port: " + port.getID();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println(portName + " is sleeping");
                Thread.sleep(SLEEP_DURATION_MS);
                generateAndProcessShips();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(portName + " is interrupted");
                return;
            }
        }
    }

    private void generateAndProcessShips() {
        String portID = port.getID() + "";
        System.out.println("Port: " + portID + " is generating a new ship...");
        new PortGenShip(port, random, tunnel).run();
        System.out.println("Port: " + portID + " is checking ships in storage...");
        new PortCheckShipStorage(port, tunnel, random).run();
    }
}

/**
 * PortCheckShipStorage
 */
class PortCheckShipStorage implements Runnable {
    private Port port;
    private Random random;
    private final Tunnel tunnel;

    public PortCheckShipStorage(Port port, Tunnel tunnel, Random random) {
        this.port = port;
        this.tunnel = tunnel;
        this.random = random;
    }

    @Override
    public void run() {
        List<Ship[]> ships = port.getShipStorage().get();

        for (Ship[] shipArr : ships) {
            for (Ship ship : shipArr) {
                if (ship != null) {
                    System.out.println("Port: " + port.getID() + " found ship to unload");
                    new PortUnloadShip(this.port, ship, random, this.tunnel).run();
                }
            }
        }

        System.out.println("Port: " + port.getID() + " cleans the storage...");
        this.port.setShipStorage(new ShipStorage(
                ShipStorageCapacity.SMALL, ShipStorageCapacity.MIDDLE, ShipStorageCapacity.BIG));
    }
}

/**
 * PortUnloadShip
 */
class PortUnloadShip implements Runnable {
    private Port port;
    private Ship ship;
    private Random random;
    private final Tunnel tunnel;

    public PortUnloadShip(Port port, Ship ship, Random random, Tunnel tunnel) {
        this.port = port;
        this.ship = ship;
        this.random = random;
        this.tunnel = tunnel;
    }

    @Override
    public void run() {
        System.out.println("Port " + port.getID() + " start unload the ship");
        Product shipProduct = new ShipProductGetter().get(ship);
        Product portProduct = this.port.getProduct();

        if (portProduct.getClass().getName().equals(shipProduct.getClass().getName())) {
            if (portProduct instanceof Gold) {
                Gold portProd = (Gold) portProduct;
                Gold shipProd = (Gold) shipProduct;

                portProd.set(portProd.get() + portProd.get());
                shipProd.set(0);
            } else if (portProduct instanceof Wood) {
                Wood portProd = (Wood) portProduct;
                Wood shipProd = (Wood) shipProduct;

                portProd.set(portProd.get() + portProd.get());
                shipProd.set(0);
            } else if (portProduct instanceof Meat) {
                Meat portProd = (Meat) portProduct;
                Meat shipProd = (Meat) shipProduct;

                portProd.set(portProd.get() + portProd.get());
                shipProd.set(0);
            }
        }

        System.out.println("Port: " + port.getID() + " sending the ship to new port");
        new Thread(new SendShip(port, ship, this.random, this.tunnel)).start();

    }
}

/**
 * PortGenShip
 */
class PortGenShip implements Runnable {
    private final Port port;
    private Random random;
    private final Tunnel tunnel;
    private static final int SHIP_COST_THRESHOLD = 100;
    private static final long SLEEP_DURATION_MS = 5000;

    public PortGenShip(Port port, Random random, Tunnel tunnel) {
        this.port = port;
        this.random = random;
        this.tunnel = tunnel;
    }

    @Override
    public void run() {
        Product product = this.port.getProduct();

        if (product.get() instanceof Integer) {
            Integer value = (Integer) product.get();
            System.out.println("Port: " + port.getID() + " is checking if it has enough product to build a new ship");
            if (value > SHIP_COST_THRESHOLD) {
                createAndSendShip();
            } else {
                System.out.println("port: " + this.port.getID() + " does not have enough money");

            }
        }

        sleepThread();
    }

    private void createAndSendShip() {
        System.out.println("port: " + this.port.getID() + " has enough product to build a ship");
        Ship newShip = this.port.getFabricShip().create(this.port.getLocation());

        System.out.println("New ship created: " + newShip.getID());
        Runnable sendShipTask = new SendShip(this.port, newShip, random, tunnel);
        new Thread(sendShipTask).start();
    }

    private void sleepThread() {
        try {
            Thread.sleep(SLEEP_DURATION_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

/**
 * SendShip
 */
class SendShip implements Runnable {
    private Port port;
    private Ship ship;
    private Random random;
    private final Tunnel tunnel;

    public SendShip(Port port, Ship ship, Random random, Tunnel tunnel) {
        this.port = port;
        this.ship = ship;
        this.random = random;
        this.tunnel = tunnel;
    }

    @Override
    public void run() {
        Port randomPort = selectRandomPort();
        ShipCapacity shipCapacity = new ShipCapacityGetter().get(ship);
        System.out.println("Sending ship to port: " + randomPort.getID());

        AppendShip appendShip = new AppendShip(shipCapacity, ship, randomPort, random, tunnel);
        // Consider using an ExecutorService or similar mechanism here
        new Thread(appendShip).start();
    }

    private Port selectRandomPort() {
        List<Port> ports = this.port.getPortDataBase().getPorts();
        return ports.get(this.random.nextInt(ports.size()));
    }
}

/**
 * AppendShip
 */
class AppendShip implements Runnable {
    private ShipCapacity shipCapacity;
    private Ship newShip;
    private Port port;
    private Random random;
    private boolean added = false;
    private final Tunnel tunnel;

    public boolean getAdded() {
        return this.added;
    }

    public AppendShip(ShipCapacity shipCapacity, Ship newShip, Port port, Random random, Tunnel tunnel) {
        this.shipCapacity = shipCapacity;
        this.newShip = newShip;
        this.port = port;
        this.random = random;
        this.tunnel = tunnel;
    }

    @Override
    public void run() {
        System.out.println("Port: " + this.port.getID() + " check if has place for the new ship");
        switch (shipCapacity) {
            case SMALL:
            case MIDDLE:
            case BIG:
                System.out.println("Ship been added to new port storage");
                Ship[] shipStorageArray = getShipStorageArray(shipCapacity);
                added = appendToArr(shipStorageArray, newShip);
                startShipRoadThread();
                break;
            default:
                handleDefaultCase();
                break;
        }
    }

    private void startShipRoadThread() {
        System.out.println("prepare road for ship to port: " + port.getID());
        Runnable shipRoadRunnable = new ShipRoad(this.tunnel, this.newShip, this.port, this.random);
        // Use an ExecutorService or similar mechanism here
        new Thread(shipRoadRunnable).start();
    }

    private void handleDefaultCase() {
        System.out.println("Port: " + port.getID() + " do not has palce for ship");
        System.out.println("sending ship to new place...");
        new SendShip(port, newShip, random, tunnel).run();
        // Logic for the default case
    }

    private Ship[] getShipStorageArray(ShipCapacity capacity) {
        switch (capacity) {
            case SMALL:
                return port.getShipStorage().getSmall();
            case MIDDLE:
                return port.getShipStorage().getMiddle();
            case BIG:
                return port.getShipStorage().getBig();
            default:
                throw new IllegalArgumentException("Invalid ship capacity");
        }
    }

    private boolean appendToArr(Ship[] arr, Ship newShip) {
        SortArr.sortArr(arr);

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                arr[i] = newShip;
                return true;
            }
        }
        return false;
    }
}

class SortArr {
    public static void sortArr(Ship[] arr) {
        Arrays.sort(arr, Comparator.nullsFirst(Comparator.comparingInt(Ship::getID)));
    }
}
