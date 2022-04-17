package service;

public interface DrawerLock {

    /**
     * Method that locks the side drawer
     * @param enabled - true when drawer is locked
     */
    void lockDrawer(boolean enabled);
}
