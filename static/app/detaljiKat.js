
Vue.component("detalji-kat", {
	data: function () {
        return {
            kategorija : "",
            ime : "",
            brJezgara : 0,
            ram : 0,
            gpuJezgara : 0,
            id:null
        }
    },
	methods : {
		checkParams: checkFormParams
        ,
        izmenaKat:function(){
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
            let promise = axios.put("/vmKategorije/"+this.kategorija.id,{
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
        },obrisi:function(){
            axios.delete('/vmKategorije/'+this.id).then(response => {
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
	mounted () {
        
        this.kategorija =  this.$route.params.kat;
        this.id =  this.$route.params.kat.id;
        this.ime =  this.$route.params.kat.ime;
        this.gpuJezgara =  this.$route.params.kat.brGPU;
        this.brJezgara =  this.$route.params.kat.brJezgra;
        this.ram =  this.$route.params.kat.RAM;
        this.tipKorisnika = this.$route.params.tipKorisnika;
        if(this.tipKorisnika!="SUPER_ADMIN"){
            $("select input").prop("readonly", true);
        }
    },
    template: ` 
<div class="container">
    <div class="row">
        <div class="page-header col-8">
            <h1>Kategorija: {{$route.params.kat.ime}}</h1>
        </div>
        <div>
            <router-link to="/">
                <button type="button" class="btn btn-primary">Nazad</button>
            </router-link>
        </div>
    </div>
    <div  class="container">
        Naziv kategorije: <input  type="text" v-bind:placeholder="kategorija.ime" v-model="ime"/>
        <p  class="alert alert-danger d-none">
            Ovo polje je obavezno!
        </p>
        <br><br>
        Broj jezgara:  <input onkeypress="return event.charCode != 45" type="number" name="quantity" min="1"  v-model="brJezgara">
        <p  class="alert alert-danger d-none">
            Ovo polje je obavezno!
        </p>
        <br><br>
        RAM (u GB):  <input onkeypress="return event.charCode != 45" type="number" name="quantity" min="1" v-model="ram">
        <p  class="alert alert-danger d-none">
            Ovo polje je obavezno!
        </p>
        <br><br>
        GPU jezgra:  <input onkeypress="return event.charCode != 45" type="number" name="quantity" min="0" v-model="gpuJezgara">
        <br><br>

        <button type="button" class="btn btn-success" @click = "izmenaKat()">Izmeni kategorije</button>
        <button type="button" class="btn btn-danger" @click = "obrisi()">Obriši kategoriju</button>
    </div>
</div>		  
`
	
});




