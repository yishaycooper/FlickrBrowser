package com.example.android.flickrbrowser;


import java.io.Serializable;

//To serialize an object means to convert its state to a byte stream so that the byte stream can be
//reverted back into a copy of the object.
public class Photo implements Serializable {
// this line allows runnig on different vertual machines
    private static final long serialVersionUID = 1L;

        private String mTitle;
        private String mAuthor;
        private String mAuthorId;
        private String mLink;
        private String mTags;
        private String mImage;

        public Photo(String title, String author, String authorId, String link, String tags, String image) {
            mTitle = title;
            mAuthor = author;
            mAuthorId = authorId;
            mLink = link;
            mTags = tags;
            mImage = image;
        }

        String getTitle() {
            return mTitle;
        }

        String getAuthor() {
            return mAuthor;
        }

        String getAuthorId() {
            return mAuthorId;
        }

        String getLink() {
            return mLink;
        }

        String getTags() {
            return mTags;
        }

        String getImage() {
            return mImage;
        }

        @Override
        public String toString() {
            return "Phto{" +
                    "mTitle='" + mTitle + '\'' +
                    ", mAuthor='" + mAuthor + '\'' +
                    ", mAuthorId='" + mAuthorId + '\'' +
                    ", mLink='" + mLink + '\'' +
                    ", mTags='" + mTags + '\'' +
                    ", mImage='" + mImage + '\'' +
                    '}';
        }
}
