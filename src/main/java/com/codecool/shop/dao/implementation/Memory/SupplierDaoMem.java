package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.JDBC.SupplierDaoJDBC;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A memory implementation of the SupplierDao Interface.
 *
 * <p>A Singleton Object</p>
 */
public class SupplierDaoMem implements SupplierDao {

    private List<Supplier> DATA = new ArrayList<>();
    private static SupplierDaoMem instance = null;
    private static Logger logger = LoggerFactory.getLogger(SupplierDaoMem.class);

    /* A private Constructor prevents any other class from instantiating.
     */
    private SupplierDaoMem() {
    }

    /**
     * @return If exist returns the SupplierDaoMem instance, if not creates it.
     */
    public static SupplierDaoMem getInstance() {
        if (instance == null) {
            instance = new SupplierDaoMem();
        }
        return instance;
    }

    /**
     * Adds the Supplier to the memory and sets an id for it.
     * @param supplier A Supplier Object.
     */
    @Override
    public void add(Supplier supplier) {
        supplier.setId(DATA.size() + 1);
        logger.info("Supplier added to memory with id: {}", supplier.getId());
        DATA.add(supplier);
    }

    /**
     *
     * @param id Id for the Supplier
     * @return Returns a Supplier if exists, if not returns null.
     */
    @Override
    public Supplier find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /**
     * Removes the Supplier from the memory.
     * @param id If for the Supplier.
     */
    @Override
    public void remove(int id) {
        DATA.remove(find(id));
        logger.info("Supplier removed from memory with id: {}", id);
    }

    /**
     * @return Returns all Supplier from the memory.
     */
    @Override
    public List<Supplier> getAll() {
        return DATA;
    }
}
