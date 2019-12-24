Vue.component("site-header", {
	data: function () {
		    return {
		      type: ""
		    }
	},
	template: ` 
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand">Cloud service</a>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="#">Virtualne mašine <span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Diskovi <span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item" v-if = "type=='super_admin'">
                    <a class="nav-link" href="#">Organizacije <span class="sr-only">(current)</span></a>
                </li>
            
                <li class="nav-item">
                    <a v-if = "type=='super_admin'||type=='admin'" class="nav-link" href="korisnici.html">Korisnici <span class="sr-only">(current)</span></a>
                </li>

                <li class="nav-item">
                    <a class="nav-link" href="#">Kategorije <span class="sr-only">(current)</span></a>
                </li>

            </ul>
        </div>
        <button v-if = "type" v-on:click = "odjava()">Odjavi se</button>

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
        axios.get('/getUserType').then(response => {
            this.type = response.data;
        }); 
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