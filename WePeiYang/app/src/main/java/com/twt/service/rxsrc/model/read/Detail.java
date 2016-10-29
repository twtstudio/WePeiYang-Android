package com.twt.service.rxsrc.model.read;

import com.twt.service.party.bean.Status;

import java.util.List;

/**
 * Created by jcy on 16-10-21.
 */

public class Detail {

    /**
     * id : 2
     * title : Bennett Borer
     * isbn : 9782662683555
     * author : Mrs. Margarett Brekke
     * press : Nitzsche-Wolff
     * time : 1993-93-20
     * index : 8679621692
     * cover_url : https://img3.doubanio.com/lpic/s4390060.jpg
     * summary : Eius repellendus eum excepturi. Non non incidunt impedit est eligendi reprehenderit. Doloribus aut dolorem suscipit totam nemo est.
     * review : {"data":[{"book_id":"2","user_name":"myrtle.ryan","avatar":"","scores":null,"like":"49","content":"Perspiciatis autem quas sed eos non tempora."},{"book_id":"2","user_name":"fdooley","avatar":"","scores":null,"like":"40","content":"Id rerum et cum magnam."},{"book_id":"2","user_name":"judah.king","avatar":"","scores":null,"like":"5","content":"Modi eos necessitatibus facilis rem officia sunt adipisci."},{"book_id":"2","user_name":"xorn","avatar":"","scores":null,"like":"40","content":"Omnis maiores non praesentium iusto error fugit quod laborum."},{"book_id":"2","user_name":"fdooley","avatar":"","scores":null,"like":"16","content":"Qui sint mollitia ratione alias veritatis doloremque deserunt quisquam."},{"book_id":"2","user_name":"jaeden44","avatar":"","scores":null,"like":"7","content":"Quae architecto deleniti ut ipsam occaecati velit."},{"book_id":"2","user_name":"cmcglynn","avatar":"","scores":null,"like":"28","content":"Quae nam quod doloribus animi minus fugiat est."},{"book_id":"2","user_name":"gudrun.cormier","avatar":"","scores":null,"like":"31","content":"Necessitatibus error culpa laudantium deserunt occaecati officiis quia."},{"book_id":"2","user_name":"rboehm","avatar":"","scores":null,"like":"30","content":"Deleniti dolores eligendi sequi architecto."},{"book_id":"2","user_name":"emery14","avatar":"","scores":null,"like":"25","content":"Facilis magni ad fuga maiores dolor eum."},{"book_id":"2","user_name":"kuhic.summer","avatar":"","scores":null,"like":"18","content":"Dignissimos est ut excepturi facere eligendi."},{"book_id":"2","user_name":"jaeden44","avatar":"","scores":null,"like":"43","content":"Quo doloremque amet quod molestias."},{"book_id":"2","user_name":"emery14","avatar":"","scores":null,"like":"21","content":"Nemo ducimus quasi quo perferendis eos nemo."},{"book_id":"2","user_name":"oconnell.ava","avatar":"","scores":null,"like":"26","content":"Quis laboriosam laboriosam dolor sit est aspernatur deleniti quis."},{"book_id":"2","user_name":"myrtle.ryan","avatar":"","scores":null,"like":"50","content":"Cupiditate quaerat et cumque sit aut blanditiis."},{"book_id":"2","user_name":"myrtle.ryan","avatar":"","scores":null,"like":"1","content":"Et voluptas eum aut maiores harum."}]}
     * starreview : {"data":[{"book_id":"2","user_name":"cmcglynn","avatar":"","like":null,"content":"Omnis quae cumque distinctio optio voluptatem ea tempora."},{"book_id":"2","user_name":"fdooley","avatar":"","like":null,"content":"Aut quos voluptas et culpa doloremque mollitia dolores."},{"book_id":"2","user_name":"gudrun.cormier","avatar":"","like":null,"content":"Magnam in ea rerum soluta qui distinctio."},{"book_id":"2","user_name":"plang","avatar":"","like":null,"content":"Sequi excepturi et dolore quidem aut."},{"book_id":"2","user_name":"nellie.nienow","avatar":"","like":null,"content":"At repudiandae et at nemo omnis sit."},{"book_id":"2","user_name":"nellie.nienow","avatar":"","like":null,"content":"Et aut ut vitae assumenda expedita nesciunt."},{"book_id":"2","user_name":"labadie.alvina","avatar":"","like":null,"content":"Facere expedita consequatur quae inventore."},{"book_id":"2","user_name":"oconnell.ava","avatar":"","like":null,"content":"Fugiat reiciendis quaerat eum libero."},{"book_id":"2","user_name":"nellie.nienow","avatar":"","like":null,"content":"Impedit quis vitae aut voluptatem quis assumenda mollitia."},{"book_id":"2","user_name":"kuhic.summer","avatar":"","like":null,"content":"Soluta impedit qui enim similique commodi et."},{"book_id":"2","user_name":"renner.bartholome","avatar":"","like":null,"content":"Rerum minus fuga eos magnam et eum alias eum."},{"book_id":"2","user_name":"labadie.alvina","avatar":"","like":null,"content":"Illo corrupti et id atque aut possimus voluptatibus."},{"book_id":"2","user_name":"oconnell.ava","avatar":"","like":null,"content":"Quaerat tempora quas velit libero vitae."},{"book_id":"2","user_name":"judah.king","avatar":"","like":null,"content":"Sed saepe ratione cupiditate voluptatem fugiat voluptas."},{"book_id":"2","user_name":"judah.king","avatar":"","like":null,"content":"Nam earum nobis placeat neque perspiciatis."},{"book_id":"2","user_name":"modesta.huel","avatar":"","like":null,"content":"Autem fuga qui laudantium veritatis."}]}
     */

    public String id;
    public String title;
    public String isbn;
    public String author;
    public String publisher;
    public String time;
    public String index;
    public String cover_url;
    public String summary;
    public ReviewBean review;
    public StarreviewBean starreview;
    public Status status;

    public static class ReviewBean {
        /**
         * book_id : 2
         * user_name : myrtle.ryan
         * avatar :
         * scores : null
         * like : 49
         * content : Perspiciatis autem quas sed eos non tempora.
         */

        public List<DataBean> data;

        public static class DataBean {
            public String book_id;
            public String user_name;
            public String avatar;
            public String scores;
            public String like;
            public String content;
        }
    }

    public static class StarreviewBean {
        /**
         * book_id : 2
         * user_name : cmcglynn
         * avatar :
         * like : null
         * content : Omnis quae cumque distinctio optio voluptatem ea tempora.
         */

        public List<DataBean> data;

        public static class DataBean {
            public String book_id;
            public String user_name;
            public String avatar;
            public Object like;
            public String content;
        }
    }

    public static class StatusBean {
        public static class DataBean{
            public String location;
            public String library;
            public String status;
        }
    }
}




    /*public String isbn;
    public String title;
    public String cover;
    public String author;
    public String publisher;
    public String year;
    public String summary;
    public String index;
    public int rate;

    public List<statusItem> status;

    public star_review starreview;

    public List<reviewItem> reviews;


    public static class star_review{
        public String username;
        public String avatar;
        public String content;
    }

    public static class statusItem{
        public String barcode;
        public String status;
        public String duetime;
        public String library;
        public String location;

    }

    public static class reviewItem{
        public String content;
        public int rate;
        public int like;
        public String timestamp;
        public user user;

        public static class user{
            public int id;
            public String name;
            public String avatar;
        }
    }*/

