Vue.component("dodaj-kat",{
    data:function () {
        return{
            ime : null,
            brJezgara : null,
            ram : null,
            gpuJezgara : null
        }
    },
    mounted:function () {
    
    },
    methods:{
        checkParams: checkFormParams
        ,
        addKat:function(){
            if(!this.checkParams()){
                return;
            }
            console.log(this.ime);
            console.log(this.brJezgara);
            console.log(this.gpuJezgara);
            if(this.gpuJezgara==""){
                this.gpuJezgara=0;
            }
            console.log(this.gpuJezgara);
            let promise = axios.post("/vmKategorije",{
                ime: this.ime,
                brJezgra: this.brJezgara,
                RAM: this.ram,
                brGPU: this.gpuJezgara,
              }
            )
            promise.then(response=>{
                this.$router.push("/");
                
            }).catch(error=>{
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message:msg,
                    type: 'danger'
                });
            });
        }
    },
    template:`
<div>
    <div class="row">
        <div class="page-header col-8">
            <h2>Dodavanje kategorije</h2>
        </div>
        <div>
            <router-link to="/">
                <button type="button" class="btn btn-primary">Nazad</button>
            </router-link>
        </div>
    </div>
    <div  class="container">
        Naziv kategorije: <input class="required" type="text" v-model="ime"/>
        <p  class="alert alert-danger d-none">
            Ovo polje je obavezno!
        </p>
        <br><br>
    Broj jezgara:  <input class="required" onkeypress="return event.charCode != 45" type="number" name="quantity" min="1" v-model="brJezgara">
        <p  class="alert alert-danger d-none">
            Ovo polje je obavezno!
        </p>
        <br><br>
        RAM (u GB):  <input class="required" onkeypress="return event.charCode != 45" type="number" name="quantity" min="1" v-model="ram">
        <p  class="alert alert-danger d-none">
            Ovo polje je obavezno!
        </p>
        <br><br>
        GPU jezgra:  <input class="required" onkeypress="return event.charCode != 45" type="number" name="quantity" min="0" v-model="gpuJezgara">
        <p  class="alert alert-danger d-none">
            Ovo polje je obavezno!
        </p>
        <br><br>
        <button  type="button" class="btn btn-success"  @click = "addKat()">Dodaj kategoriju</button>
    </div>
</div>
    `
});







