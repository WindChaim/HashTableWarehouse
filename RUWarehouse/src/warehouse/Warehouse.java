package warehouse;

/*
 *
 * This class implements a warehouse on a Hash Table like structure, 
 * where each entry of the table stores a priority queue. 
 * Due to your limited space, you are unable to simply rehash to get more space. 
 * However, you can use your priority queue structure to delete less popular items 
 * and keep the space constant.
 * 
 * @author Ishaan Ivaturi
 */ 
public class Warehouse {
    private Sector[] sectors;
    
    // Initializes every sector to an empty sector
    public Warehouse() {
        sectors = new Sector[10];

        for (int i = 0; i < 10; i++) {
            sectors[i] = new Sector();
        }
    }
    
    /**
     * Provided method, code the parts to add their behavior
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void addProduct(int id, String name, int stock, int day, int demand) {
        evictIfNeeded(id);
        addToEnd(id, name, stock, day, demand);
        fixHeap(id);
    }

    /**
     * Add a new product to the end of the correct sector
     * Requires proper use of the .add() method in the Sector class
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    private void addToEnd(int id, String name, int stock, int day, int demand) {
        Product newProduct = new Product(id, name, stock, day, demand);
        int idSector = id%10;
        sectors[idSector].add(newProduct);
    }

    /**
     * Fix the heap structure of the sector, assuming the item was already added
     * Requires proper use of the .swim() and .getSize() methods in the Sector class
     * @param id The id of the item which was added
     */
    private void fixHeap(int id) {
        int idSector = id%10;
        while (sectors[idSector].getSize() != 1){
        int size = sectors[idSector].getSize();
        if (sectors[idSector].get(size) != null && sectors[idSector].get(size/2) != null){
        sectors[idSector].swim(size);
        }
        break;
        }
    }

    /**
     * Delete the least popular item in the correct sector, only if its size is 5 while maintaining heap
     * Requires proper use of the .swap(), .deleteLast(), and .sink() methods in the Sector class
     * @param id The id of the item which is about to be added
     */
    private void evictIfNeeded(int id) {
        int idSector = id%10;
        int size = sectors[idSector].getSize();
        if (size == 5){
            sectors[idSector].swap(1,5);
            sectors[idSector].deleteLast();
            sectors[idSector].sink(1);
        }
    }

    /**
     * Update the stock of some item by some amount
     * Requires proper use of the .getSize() and .get() methods in the Sector class
     * Requires proper use of the .updateStock() method in the Product class
     * @param id The id of the item to restock
     * @param amount The amount by which to update the stock
     */
    public void restockProduct(int id, int amount) {
        int idSector = id%10;
        int size = sectors[idSector].getSize();
        for (int i = size; i > 0; i--){
            if(sectors[idSector].get(i).getId() == id){
                sectors[idSector].get(i).updateStock(amount);
                break;
            }

        }
    }
    
    /**
     * Delete some arbitrary product while maintaining the heap structure in O(logn)
     * Requires proper use of the .getSize(), .get(), .swap(), .deleteLast(), .sink() and/or .swim() methods
     * Requires proper use of the .getId() method from the Product class
     * @param id The id of the product to delete
     */
    public void deleteProduct(int id) {
        int idSector = id%10;
        int size = sectors[idSector].getSize();
        int lastPointer = size;

        for (int i = size; i > 0; i--){
                if(sectors[idSector].get(i).getId() == id){
                    sectors[idSector].swap(i, lastPointer);
                    sectors[idSector].deleteLast();
                    sectors[idSector].sink(i);
                    break;
                }
                
            }
        }
    
    /**
     * Simulate a purchase order for some product
     * Requires proper use of the getSize(), sink(), get() methods in the Sector class
     * Requires proper use of the getId(), getStock(), setLastPurchaseDay(), updateStock(), updateDemand() methods
     * @param id The id of the purchased product
     * @param day The current day
     * @param amount The amount purchased
     */
    public void purchaseProduct(int id, int day, int amount) {
        int idSector = id%10;
        int size = sectors[idSector].getSize();
        for (int i = size; i > 0; i--){
            if (sectors[idSector].get(i) != null)
            if(sectors[idSector].get(i).getId() == id){
                if (sectors[idSector].get(i).getStock() >= amount){
                sectors[idSector].get(i).setLastPurchaseDay(day);
                sectors[idSector].get(i).updateStock(-amount);
                sectors[idSector].get(i).updateDemand(amount);
                sectors[idSector].sink(i);
                break;
                }
            }
        }
    }
    
    /**
     * Construct a better scheme to add a product, where empty spaces are always filled
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void betterAddProduct(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
    }

    /*
     * Returns the string representation of the warehouse
     */
    public String toString() {
        String warehouseString = "[\n";

        for (int i = 0; i < 10; i++) {
            warehouseString += "\t" + sectors[i].toString() + "\n";
        }
        
        return warehouseString + "]";
    }

    /*
     * Do not remove this method, it is used by Autolab
     */ 
    public Sector[] getSectors () {
        return sectors;
    }
}
