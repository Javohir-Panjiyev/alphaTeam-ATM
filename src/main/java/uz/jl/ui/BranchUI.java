package uz.jl.ui;

import java.util.Objects;

/**
 * @author Elmurodov Javohir, Wed 4:45 PM. 12/8/2021
 */
public class BranchUI implements BaseUI{

    private static BranchUI branchUI;

    public static BranchUI getInstance() {
        if (Objects.isNull(branchUI)){
            return branchUI=new BranchUI();
        }
        return branchUI;
    }

    private BranchUI() {
    }

    @Override
    public void create() {

    }

    @Override
    public void block() {

    }

    @Override
    public void unblock() {

    }

    @Override
    public void delete() {

    }

    @Override
    public boolean list() {
        return false;
    }
}
