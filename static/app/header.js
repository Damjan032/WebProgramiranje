Vue.component("site-header", {
	data: function () {
		    return {
		      loggedin: false
		    }
	},
	template: ` 
    <nav class="navbar">
        <a class="navbar-brand">Cloud service</a>
        <button v-if = "loggedin==true" v-on:click = "odjava()">Odjavi se</button>
    </nav>	  
`
	, 
	methods : {
        odjava:function () {
            axios.get('/logout').then(response => {
                if(response.data.status){
                    window.location.replace("/login.html");
                }else{
                    new Toast({
                        message:response.data.poruka,
                        type: 'danger'
                    });
                }
            })
        }
	},
	mounted () {
        axios.get('/isloggedin').then(response => {
            this.loggedin = response.data;
        })   
    }
});



// Vue.component("header", function (resolve, reject) {
//     vue.$http.get("header.html", function (data, status, request) {
//        var parser = new DOMParser();
//        var doc = parser.parseFromString(data, "text/html");
//        resolve({
//            template: doc
//        }); 
//     });
// });


// let headerapp = new Vue({
//     el:"#header",
//     data: {
//         loggedin : false
//     },
//     mounted () {
//         axios.get('/isloggedin').then(response => {
//             loggedin = response.data;})    
//     }
// });