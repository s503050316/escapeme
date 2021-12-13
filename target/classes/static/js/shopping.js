const GetShoppingUrl = "http://localhost:8080/shopping/findCommodity";
// 導覽列頭像
const GetNavUserImgUrl = "/api1/custo/findMemberImage";

// ====================================
//          前端測試 URL
// ====================================
// // 獲取商品
// const GetShoppingUrl = "http://0.0.0.0:3000/shopping";
// // 導覽列頭像
// const GetNavUserImgUrl = "http://0.0.0.0:3000/EscapeME_userImg";
// ====================================

var shopVm = new Vue({
  el: "#app",
  data: {
    shoppingList: [],
    req: {
      star: null,
      starTop: null,
      type: null,
      title: null,
    },
    // 導覽列頭像
    userImgSrc: "",
  },
  methods: {
    typeClick(type) {
      this.req.type = type;
      // console.log(this.req.type)
      fetch(GetShoppingUrl, {
        method: "POST",
        body: JSON.stringify(this.req),
        headers: new Headers({
          "Content-Type": "application/json",
        }),
      })
        .then((res) => res.json())
        .catch((error) => console.error("Error:", error))
        .then((response) => {
          console.log("Success:", response);
          this.shoppingList = response;
        });
    },
    starClick(star, starTop) {
      this.req.star = star;
      this.req.starTop = starTop;
      fetch(GetShoppingUrl, {
        method: "POST",
        body: JSON.stringify(this.req),
        headers: new Headers({
          "Content-Type": "application/json",
        }),
      })
        .then((res) => res.json())
        .catch((error) => console.error("Error:", error))
        .then((response) => {
          console.log("Success:", response);
          this.shoppingList = response;
        });
    },
    allClick(star, starTop, title, type) {
      this.req.star = star;
      this.req.starTop = starTop;
      this.req.title = title;
      this.req.type = type;
      fetch(GetShoppingUrl, {
        method: "POST",
        body: JSON.stringify(this.req),
        headers: new Headers({
          "Content-Type": "application/json",
        }),
      })
        .then((res) => res.json())
        .catch((error) => console.error("Error:", error))
        .then((response) => {
          console.log("Success:", response);
          this.shoppingList = response;
        });
    },
  },
  mounted() {
    fetch(GetShoppingUrl, {
      method: "POST",
      body: JSON.stringify(this.req),
      headers: new Headers({
        "Content-Type": "application/json",
      }),
    })
      .then((res) => res.json())
      .catch((error) => console.error("Error:", error))
      .then((response) => {
        console.log("Success:", response);
        this.shoppingList = response;
      });

      // 導覽列頭像
      fetch(GetNavUserImgUrl)
      .then((res) => res.json())
      .catch((error) => console.error("Error:", error))
      .then((response) => {
        console.log("Success:", response);
        this.userImgSrc = response.userImgSrc;
      });
  },
});
