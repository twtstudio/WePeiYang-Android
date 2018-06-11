package com.twtstudio.service.dishesreviews.model

/**
 * Created by SGXM on 2018/5/16.
 */
/////////////////////////////////////Home//////////////////////////////////////
data class DishesHomeBean(
        val top10Food: List<Food> = listOf(),
        val canteenList: List<Canteen> = listOf(),
        val goodComment: List<GoodComment> = listOf()
)

data class GoodComment(
        val comment_id: Int = 0, //36
        val food_name: String = "", //null
        val food_id: Int = 0, //15
        val commenter_id: Int = 0, //1
        val commenter_name: String = "", //Dean
        val food_time: String = "", //午餐
        val food_score: Int = 0, //5
        val comment_content: String = "", //这个东西太和我的胃口了
        val comment_praise_number: Int = 0, //1
        val comment_is_anonymous: Int = 0, //0
        val picture_address1: String = "",
        val picture_address2: String = "",
        val picture_address3: String = "",
        val picture_address4: String = "",
        val created_at: String = "", //2018-05-05 13:05:05
        val deleted_at: Any = Any(), //null
        val updated_at: String = "", //2018-05-05 14:35:39
        val isCurrentUserPraised: Boolean = false //true
)

data class Canteen(
        val canteen_id: Int = 0, //18
        val canteen_name: String = "", //竹园
        val canteen_phone: Int = 0, //1425367987
        val canteen_address: String = "", //天大北洋园
        val canteen_time: String = "", //bantian
        val canteen_floor: Int = 0, //2
        val created_at: String = "", //2018-05-05 12:19:54
        val updated_at: String = "", //2018-05-05 12:19:54
        val deleted_at: Any = Any(), //null
        val canteen_picture_address: String = "" //null
)
/////////////////////////////////////Home/////////////////////////////////////

/////////////////////////////////////搜索页面////////////////////////////////////

data class DishesSearchBean(
        val searchResult: List<Food> = listOf()
)

/////////////////////////////////////搜索页面////////////////////////////////////

/////////////////////////////////////食堂页/////////////////////////////////////


data class DishesCanteenBean(
        val canteenInfo: CanteenInfo = CanteenInfo(),
        val canteenScore: Int = 0, //5
        val firstFloor: Floor = Floor(),
        val secondFloor: Floor = Floor()
)

data class CanteenInfo(
        val canteen_id: Int = 0, //15
        val canteen_name: String = "", //梅园
        val canteen_phone: String = "", //1425367987
        val canteen_address: String = "", //天大北洋园
        val canteen_time: String = "", //全天
        val canteen_floor: Int = 0, //2
        // val created_at: String = "", //2018-05-05 12:18:16
        //val updated_at: String = "", //2018-05-05 12:18:16
        // val deleted_at: Any = Any(), //null
        val canteen_picture_address: String = "" //../public/food/img/梅园.jpg
)

data class Floor(
        val foodRecommend: List<FoodRecommend>? = null,
        val foodList: List<Food>? = null,
        val latestFood: List<LatestFood>? = null
)

data class FoodRecommend(
        val food_id: Int = 0, //15
        val food_statement: String = "", //中国名菜
        val food_name: String = "", //鱼香鸡丝
        val food_time: String = "", //晚餐
        val food_price: Int = 0, //10
        val food_picture_address: String = "", //../public/food/img/food15.jpg
        val canteen_id: Int = 0, //15
        val canteen_name: String = "", //梅园
        val food_floor: String = "", //1
        val food_window: Int = 0, //1
        val food_comment_number: Int = 0, //7
        val food_praise_number: Int = 0, //1
        val food_total_score: Int = 0, //26
        val food_score: Int = 0, //4
        val food_collect_number: Int = 0, //1
        val created_at: String = "", //2018-05-05 12:35:21
        val deleted_at: Any = Any(), //null
        val updated_at: String = "" //2018-05-23 23:46:54
)

data class LatestFood(
        val food_id: Int = 0, //15
        val food_statement: String = "", //中国名菜
        val food_name: String = "", //鱼香鸡丝
        val food_time: String = "", //晚餐
        val food_price: Int = 0, //10
        val food_picture_address: String = "", //../public/food/img/food15.jpg
        val canteen_id: Int = 0, //15
        val canteen_name: String = "", //梅园
        val food_floor: String = "", //1
        val food_window: Int = 0, //1
        val food_comment_number: Int = 0, //7
        val food_praise_number: Int = 0, //1
        val food_total_score: Int = 0, //26
        val food_score: Int = 0, //4
        val food_collect_number: Int = 0, //1
        val created_at: String = "", //2018-05-05 12:35:21
        val deleted_at: Any = Any(), //null
        val updated_at: String = "" //2018-05-23 23:46:54
)

data class Food(
        val food_id: Int = 0, //15
        val food_statement: String = "", //中国名菜
        val food_name: String = "", //鱼香鸡丝
        val food_time: String = "", //晚餐
        val food_price: Int = 0, //10
        val food_picture_address: String = "", //../public/food/img/food15.jpg
        val canteen_id: Int = 0, //15
        val canteen_name: String = "", //梅园
        val food_floor: String = "", //1
        val food_window: Int = 0, //1
        val food_comment_number: Int = 0, //7
        val food_praise_number: Int = 0, //1
        val food_total_score: Int = 0, //26
        val food_score: Int = 0, //4
        val food_collect_number: Int = 0, //1
        val created_at: String = "", //2018-05-05 12:35:21
        val deleted_at: Any = Any(), //null
        val updated_at: String = "" //2018-05-23 23:46:54
)

data class SecondFloor(
        val foodRecommend: Any = Any(), //null
        val foodList: Any = Any(), //null
        val latestFood: Any = Any() //null
)


//data class DishesCanteenBean(
//		val canteenInfo: CanteenInfo = CanteenInfo(),
//		val canteenScore: Int = 0, //5
//		val firstFloor: Floor = Floor(),
//		val secondFloor: Floor = Floor()
//)
//
//data class CanteenInfo(
//		val canteen_id: Int = 0, //15
//		val canteen_name: String = "", //梅园
//		val canteen_phone: Int = 0, //1425367987
//		val canteen_address: String = "", //天大北洋园
//		val canteen_time: String = "", //全天
//		val canteen_floor: Int = 0, //2
//		val created_at: String = "", //2018-05-05 12:18:16
//		val updated_at: String = "", //2018-05-05 12:18:16
//		val deleted_at: String = "", //null
//		val canteen_picture_address: String = "" //../public/food/img/梅园.jpg
//)
//
//data class Floor(
//		val foodRecommend: List<Food>? = listOf(),
//		val foodList: List<Food>? = listOf(),
//		val latestFood: List<Food>? = listOf()
//)
//
//
//data class Food(
//		val food_id: Int = 0, //15
//		val food_statement: String = "", //中国名菜
//		val food_name: String = "", //鱼香鸡丝
//		val food_time: String = "", //晚餐
//		val food_price: Int = 0, //10
//		val food_picture_address: String = "", //../public/food/img/food15.jpg
//		val canteen_id: Int = 0, //15
//		val canteen_name: String = "", //梅园
//		val food_floor: String = "", //1
//		val food_window: Int = 0, //1
//		val food_comment_number: Int = 0, //7
//		val food_praise_number: Int = 0, //1
//		val food_total_score: Int = 0, //26
//		val food_score: Int = 0, //4
//		val food_collect_number: Int = 0, //1
//		val created_at: String = "", //2018-05-05 12:35:21
//		val deleted_at: String = "", //null
//		val updated_at: String = "" //2018-05-23 23:46:54
//)


//
//data class DishesCanteenBean(
//        val canteenInfo: CanteenInfo = CanteenInfo(),
//        val canteenScore: Int = 0, //5
//        val firstFloor: FirstFloor = FirstFloor(),
//        val secondFloor: SecondFloor = SecondFloor()
//)
//
//data class CanteenInfo(
//        val canteen_id: Int = 0, //15
//        val canteen_name: String = "", //梅园
//        val canteen_phone: Int = 0, //1425367987
//        val canteen_address: String = "", //天大北洋园
//        val canteen_time: String = "", //全天
//        val canteen_floor: Int = 0, //2
//        val created_at: String = "", //2018-05-05 12:18:16
//        val updated_at: String = "", //2018-05-05 12:18:16
//        val deleted_at: Any = Any(), //null
//        val canteen_picture_address: String = "" //../public/food/img/梅园.jpg
//)
//
//open class Floor {
//    open val foodRecommend: List<FoodRecommend>? = listOf()
//    open val foodList: List<Food>? = listOf()
//    open val latestFood: List<LatestFood>? = listOf()
//}
//
//class FirstFloor : Floor(){
//    override val foodRecommend: List<FoodRecommend> = listOf()
//    override val foodList: List<Food> = listOf()
//    override val latestFood: List<LatestFood> = listOf()
//}
//
//data class FoodRecommend(
//        val food_id: Int = 0, //15
//        val food_statement: String = "", //中国名菜
//        val food_name: String = "", //鱼香鸡丝
//        val food_time: String = "", //晚餐
//        val food_price: Int = 0, //10
//        val food_picture_address: String = "", //../public/food/img/food15.jpg
//        val canteen_id: Int = 0, //15
//        val canteen_name: String = "", //梅园
//        val food_floor: String = "", //1
//        val food_window: Int = 0, //1
//        val food_comment_number: Int = 0, //7
//        val food_praise_number: Int = 0, //1
//        val food_total_score: Int = 0, //26
//        val food_score: Int = 0, //4
//        val food_collect_number: Int = 0, //1
//        val created_at: String = "", //2018-05-05 12:35:21
//        val deleted_at: Any = Any(), //null
//        val updated_at: String = "" //2018-05-23 23:46:54
//)
//
//data class LatestFood(
//        val food_id: Int = 0, //15
//        val food_statement: String = "", //中国名菜
//        val food_name: String = "", //鱼香鸡丝
//        val food_time: String = "", //晚餐
//        val food_price: Int = 0, //10
//        val food_picture_address: String = "", //../public/food/img/food15.jpg
//        val canteen_id: Int = 0, //15
//        val canteen_name: String = "", //梅园
//        val food_floor: String = "", //1
//        val food_window: Int = 0, //1
//        val food_comment_number: Int = 0, //7
//        val food_praise_number: Int = 0, //1
//        val food_total_score: Int = 0, //26
//        val food_score: Int = 0, //4
//        val food_collect_number: Int = 0, //1
//        val created_at: String = "", //2018-05-05 12:35:21
//        val deleted_at: Any = Any(), //null
//        val updated_at: String = "" //2018-05-23 23:46:54
//)
//
//data class Food(
//        val food_id: Int = 0, //15
//        val food_statement: String = "", //中国名菜
//        val food_name: String = "", //鱼香鸡丝
//        val food_time: String = "", //晚餐
//        val food_price: Int = 0, //10
//        val food_picture_address: String = "", //../public/food/img/food15.jpg
//        val canteen_id: Int = 0, //15
//        val canteen_name: String = "", //梅园
//        val food_floor: String = "", //1
//        val food_window: Int = 0, //1
//        val food_comment_number: Int = 0, //7
//        val food_praise_number: Int = 0, //1
//        val food_total_score: Int = 0, //26
//        val food_score: Int = 0, //4
//        val food_collect_number: Int = 0, //1
//        val created_at: String = "", //2018-05-05 12:35:21
//        val deleted_at: Any = Any(), //null
//        val updated_at: String = "" //2018-05-23 23:46:54
//)
//
//class SecondFloor : Floor(){
//    override val foodRecommend: List<FoodRecommend>? = listOf()
//    override val foodList: List<Food>? = listOf()
//    override val latestFood: List<LatestFood>? = listOf()
//}
/////////////////////////////////////食堂页/////////////////////////////////////

/////////////////////////////////////菜品页/////////////////////////////////////

data class DishesFoodBean(
        val foodInfo: FoodInfo = FoodInfo(),
        val foodMark: FoodMark = FoodMark(),
        val isPraisedFood: Boolean = false, //true
        val isCollectedFood: Boolean = false, //true
        val comment: List<Comment> = listOf()
)

data class FoodInfo(
        val food_id: Int = 0, //15
        val food_statement: String = "", //中国名菜
        val food_name: String = "", //鱼香鸡丝
        val food_time: String = "", //晚餐
        val food_price: Int = 0, //10
        val food_picture_address: String = "", //null
        val canteen_id: Int = 0, //15
        val canteen_name: String = "", //梅园
        val food_floor: String = "", //1
        val food_window: Int = 0, //1
        val food_comment_number: Int = 0, //3
        val food_praise_number: Int = 0, //1
        val food_total_score: Int = 0, //15
        val food_score: Int = 0, //5
        val food_collect_number: Int = 0, //1
        val created_at: String = "", //2018-05-05 12:35:21
        val deleted_at: Any = Any(), //null
        val updated_at: String = "" //2018-05-05 14:34:02
)

data class FoodMark(
        val spicy: Int = 0, //3
        val fine: Int = 0, //3
        val attitude: Int = 0 //3
)

data class Comment(
        val comment_id: Int = 0, //36
        val food_id: Int = 0, //15
        val commenter_id: Int = 0, //1
        val commenter_name: String = "", //Dean
        val food_time: String = "", //午餐
        val food_score: Int = 0, //5
        val comment_content: String = "", //这个东西太和我的胃口了
        val comment_praise_number: Int = 0, //1
        val comment_is_anonymous: Int = 0, //0
        val picture_address1: String = "",
        val picture_address2: String = "",
        val picture_address3: String = "",
        val picture_address4: String = "",
        val created_at: String = "", //2018-05-05 13:05:05
        val deleted_at: Any = Any(), //null
        val updated_at: String = "", //2018-05-05 14:35:39
        val isCurrentUserPraised: Boolean = false //true
)

/////////////////////////////////////菜品页/////////////////////////////////////

/////////////////////////////////////评价页/////////////////////////////////////

data class DishesEvaluateBean(
        val comment: EvaComment = EvaComment(),
        val commentMark: List<CommentMark> = listOf()
)

data class CommentMark(
        val food_id: Int = 0, //11
        val comment_id: Int = 0, //34
        val mark_id: Int = 0 //1
)

data class EvaComment(
        val food_id: String = "", //11
        val commenter_id: String = "", //2
        val commenter_name: Any = Any(), //null
        val food_time: String = "", //早餐
        val food_score: String = "", //4
        val comment_content: String = "", //这个东西太和我的胃口了
        val comment_is_anonymous: String = "", //0
        val comment_praise_number: Int = 0, //0
        val updated_at: String = "", //2018-05-03 20:27:21
        val created_at: String = "", //2018-05-03 20:27:21
        val comment_id: Int = 0 //34
)
/////////////////////////////////////评价页/////////////////////////////////////

/////////////////////////////////////添加菜品/////////////////////////////////////

data class DishesAddFoodBean(
        val food_name: String = "", //鱼香鸡丝
        val food_statement: String = "", //中国名菜
        val food_time: String = "", //晚餐
        val food_price: String = "", //10
        val food_picture_address: String = "", //null
        val canteen_id: String = "", //6
        val canteen_name: String = "", //梅园
        val food_floor: String = "", //1
        val food_window: String = "", //1
        val food_comment_number: Int = 0, //0
        val food_praise_number: Int = 0, //0
        val food_total_score: Int = 0, //0
        val food_score: Int = 0, //0
        val updated_at: String = "", //2018-05-03 21:03:27
        val created_at: String = "", //2018-05-03 21:03:27
        val food_id: Int = 0 //12
)
/////////////////////////////////////添加菜品/////////////////////////////////////

/////////////////////////////////////点赞/////////////////////////////////////
data class DishesLikeBean(
        val food_id: Int = 0, //11
        val food_statement: String = "", //中国名菜
        val food_name: String = "", //鱼香鸡丝
        val food_time: String = "", //晚餐
        val food_price: Int = 0, //10
        val food_picture_address: String = "", //null
        val canteen_id: Int = 0, //6
        val canteen_name: String = "", //梅园
        val food_floor: String = "", //1
        val food_window: Int = 0, //1
        val food_comment_number: Int = 0, //3
        val food_praise_number: Int = 0, //1
        val food_total_score: Int = 0, //12
        val food_score: Int = 0, //4
        val food_collect_number: Any = Any(), //null
        val created_at: String = "", //2018-05-03 20:20:49
        val deleted_at: Any = Any(), //null
        val updated_at: String = "" //2018-05-03 21:05:21
)
/////////////////////////////////////点赞/////////////////////////////////////

/////////////////////////////////////收藏菜品/////////////////////////////////////

//api有问题
//data class DishesCollectBean(
//		val error_code: Int = 0, //-1
//		val message: String = "",
//		val data: List<Data> = listOf()
//)
//
//data class Data(
//		val user_id: String = "", //2
//		val food_id: String = "", //11
//		val updated_at: String = "", //2018-05-03 21:06:01
//		val created_at: String = "", //2018-05-03 21:06:01
//		val collect_id: Int = 0 //10
//)
/////////////////////////////////////收藏菜品/////////////////////////////////////

/////////////////////////////////////个人主页 我的评论/////////////////////////////////////


data class DishesAccountBean(
        val commentNumber: Int = 0, //4
        val comment: List<MyComment> = listOf()
)

data class MyComment(
        val comment_id: Int = 0, //15
        val food_id: Int = 0, //5
        val commenter_id: Int = 0, //1
        val commenter_name: Any = Any(), //null
        val food_time: String = "", //早餐
        val food_score: Int = 0, //4
        val comment_content: String = "", //这个东西太和我的胃口了
        val comment_praise_number: Int = 0, //0
        val comment_is_anonymous: Int = 0, //0
        val picture_address1: Any = Any(), //null
        val picture_address2: Any = Any(), //null
        val picture_address3: Any = Any(), //null
        val picture_address4: Any = Any(), //null
        val created_at: String = "", //2018-05-03 00:13:26
        val deleted_at: Any = Any(), //null
        val updated_at: String = "" //2018-05-03 00:13:26
)
/////////////////////////////////////个人主页 我的评论/////////////////////////////////////
/////////收藏菜品/////////////////////////////////////