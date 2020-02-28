
Vue.component("prijava",
{
    data:function () {
        return{    
            kime : "",
            sifra :""
        }
    },
    mounted:function() {        
        axios.get("/korisnik")
            .then(res=>{
                if(res.data){
                    this.$router.push("vm");
                    window.location.reload();
                }
            })
            .catch(error=>
                {
                    new Toast({
                        message:error.response.data.ErrorMessage,
                        type: 'danger'
                    });
                }
        );
    },
    methods:{
        checkParams: checkFormParams
        ,

        login:function(k, s) {
            if(!this.checkParams()){
                return;
            }
            let promise = axios.get("/login",{params: {
                kime:k,
                sifra:s
              }
             }
            )
            promise.then(response=>{
                   
                    if (response.data.status) {
                        this.$router.push("vm");
                        bus.$emit("korisnik", response.data.k);
                        // window.location.replace("/vm");
                    }else{
                        new Toast({
                            message:response.data.poruka,
                            type: 'danger'
                        });
                    }

            });
        }
    },
    template:
`
<div>
    <div class="jumbotron"><h1>Prijava korisnika</h1></div>
    <div class="container">
        <div class="page-header">
            <h2>Prijavite se</h2>
        </div>
        <p id="login" v-on:keyup.enter = "login(kime, sifra)">
            <table>
                <tr>
                    <td>
                        Korisničko ime
                    </td>
                    <td>
                        <input class="required" type="text" v-model="kime"/>
                    </td>
                    <td >
                        <p  class="alert alert-danger d-none">
                            Ovo polje je obavezno!
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        Šifra
                    </td>
                    <td>
                        <input class="required" type="password" v-model="sifra"/>
                    </td>
                    <td>
                        <p  class="alert alert-danger d-none">
                            Ovo polje je obavezno!
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                    
                    </td>
                    <td>
                        <button @click = "login(kime, sifra)">Prijavite se</button>
                    </td>
                </tr> 
            </table>

            
            
        </p>
    </div>

</div>    
    
`


});