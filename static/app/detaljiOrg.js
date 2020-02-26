
Vue.component("detalji-org", {
    data: function () {
        return {
            organizacija:null,
            oId:"",
            oIme : "",
            oOpis : "",
            slika : ""
        }
    },
	methods : {
		checkParams: checkFormParams
        ,
        onFileSelected(event){
            this.slika = event.target.files[0];
        },

        izmenaOrg:function(imeOrg, opis){
            if(!this.checkParams()){
                return;
            }
            // console.log(this.slika.name);
            let data = new FormData();
            data.append("nazivSlike", this.slika.name)
            data.append('oIme', imeOrg);
            data.append('oOpis', opis);
            data.append('oSlika',this.slika);
            let promise = axios.put('/organizacije/'+this.organizacija.id, data, { headers: {'Content-Type': 'multipart/form-data'}});

            promise.then(response=>{
                this.$router.push("/");
            }).catch(error=>{
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message:msg,
                    type: 'danger'
                });
            });
        },
        obrisiOrg:function(){
            axios.delete('/organizacije/'+this.oId).then(response => {
                this.$router.push("/");
            }).catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            });
        },
        back:function () {
            this.$router.go(-1);
        }
    },
	mounted () {
        this.organizacija = this.$route.params.org;
        this.oIme =  this.$route.params.org.ime;
        this.oOpis =  this.$route.params.org.opis;
        this.slika =  this.$route.params.org.slika;
        this.oId =  this.$route.params.org.id;
        this.tipKorisnika = this.$route.params.tipKorisnika;
        if(this.tipKorisnika=="KORISNIK"){
            $("select input").prop("readonly", true);
        }
    },
    template: ` 
<div class="container">
   
    <div class="row">
        <div  class="col container">
            <div class="row">
                <div class="page-header col-8">
                    <h1>Organizacija : {{oIme}}</h1>
                </div>
                <div>
                    <button type="button" class="btn btn-primary" @click="back">Nazad</button>
                </div>
            </div>
            Naziv organizacije: <input class="required" type="text" v-model="oIme" v-bind:placeholder="organizacija.ime"/>
            <p  class="alert alert-danger d-none">
                Ovo polje je obavezno!
            </p>
            <br><br>
            Opis organizacije: <input class="required" type="text" v-model="oOpis" v-bind:placeholder="organizacija.opis"/>
            <p  class="alert alert-danger d-none">
                Ovo polje je obavezno!
            </p>
            <br><br>
            Odaberi sliku:  <input @change= "onFileSelected" class="slika required" type="file"  name="slika" accept="image/*"  v-bind:placeholder="organizacija.imgPath"/>
            <p  class="alert alert-danger d-none">
                Ovo polje je obavezno!
            </p>
            <br><br>
            
            <button v-on:click = "izmenaOrg(oIme,oOpis)" type="button" class="btn btn-success">Izmeni organizaciju</button>
            <button v-on:click = "obrisiOrg()" type="button" class="btn btn-danger">Obriši organizaciju</button>
        
        </div> 
        <div class="col">
            <h3>
                Resursi
            </h3>
            <h4 v-if="organizacija.resursi.length==0">
                Trenutno nema resursa u ovoj organizaciji
            </h4>
            <table class="table" v-else>                
                <tr>
                    <th>
                        Ime
                    </th>
                    <th>
                        Tip
                    </th>
                </tr>
                <tr v-for="resurs in organizacija.resursi">
                    <td>
                        {{resurs.ime}}
                    </td>
                    <td>
                        {{resurs.tipResursa}}
                    </td>
                </tr>
            </table>  
        </div>
    </div>
</div>		  
`
	
});




