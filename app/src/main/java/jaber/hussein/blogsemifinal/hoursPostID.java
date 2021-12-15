package jaber.hussein.blogsemifinal;

import com.google.firebase.firestore.Exclude;

import io.reactivex.annotations.NonNull;

public class hoursPostID {
    @Exclude
    public String hoursPostId;
    public <T extends hoursPostID> T withId(@NonNull final String id){
        this.hoursPostId=id;
        return (T) this;
    }
}
