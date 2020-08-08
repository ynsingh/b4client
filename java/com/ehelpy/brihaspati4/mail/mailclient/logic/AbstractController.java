package com.ehelpy.brihaspati4.mail.mailclient.logic;

// This abstract class will help the controllers classes talk to each other smoothly,
// it helps in accessing same model object access by two controllers
// This is the starting point of all controllers. Other controllers inherit/extend this controller.
public abstract class AbstractController {
    // only object to access the storage/model
    private AccessStore storAccess;

    // Constructor
    public AbstractController(AccessStore storAccess) {
        this.storAccess = storAccess;
    }


    // Getter to get the storage data
    public AccessStore getStoreAccess(){
        return storAccess;
    }

}