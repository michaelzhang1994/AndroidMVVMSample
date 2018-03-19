package aurora.mvvm.zwh.me.eyepetizer.mvp.model.entity;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {
    private BmobFile icon;

    public User(BmobUser bmobUser){
        this.setUsername(bmobUser.getUsername());
    }
    public User(){
    }

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }
}
