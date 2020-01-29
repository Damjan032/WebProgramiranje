Vue.component("dodaj-org",{
    data:function () {
        return{
            oIme : "",
            oOpis : "",
            slika : ""
        }
    },
    mounted:function () {
        axios.get('/virtuelneMasine').then(response => {
            this.vmasine = response.data;
        }).catch(error=>{
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message:msg,
                type: 'danger'
            });
        }); 
        axios.get('/organizacije').then(response => {
            this.organizacije = response.data;
        }).catch(error=>{
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message:msg,
                type: 'danger'
            });
        }); 
    },
    methods:{
        checkParams: checkFormParams
        ,
        checkParams: function(){
            let inputs = document.getElementsByTagName("input");
            let messages = document.getElementsByClassName("alert");
            let flag = true;
            for(let i = 0;i<inputs.length;i++){
                let inp = inputs[i];
                let msg = messages[i];
                if (!inp.value) {
                    // inp.classList.add("alert-danger")
                    msg.classList.remove("d-none");
                    flag = false;
                }else{
                    // inp.classList.remove("alert-danger");
                    msg.classList.add("d-none");
                }
            }
            return flag;
        },

        onFileSelected(event){
            this.slika = event.target.files[0];
        },

        addOrganizaciju:function(ime, opis){
            if(!this.checkParams()){
                return;
            }
            // console.log(this.slika.name);
            let data = new FormData();
            data.append("nazivSlike", this.slika.name)
            data.append('oIme', ime);
            data.append('oOpis', opis);
            data.append('oSlika',this.slika);
            let promise = axios.post('/organizacije', data, { headers: {'Content-Type': 'multipart/form-data'}});

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
        <h2>Nova organizacija</h2>
    </div>
    <div>
        <router-link to="/">
            <button type="button" class="btn btn-primary">Nazad</button>
        </router-link>
    </div>
</div>
<div class = "container">
    Naziv organizacije: <input class="required" type="text" v-model="oIme"/>
    <p  class="alert alert-danger d-none">
        Ovo polje je obavezno!
    </p>
    <br><br>
    Opis organizacije: <input class="required" type="text" v-model="oOpis"/>
    <p  class="alert alert-danger d-none">
        Ovo polje je obavezno!
    </p>
    <br><br>
    Odaberi sliku:  <input @change= "onFileSelected" class="slika" type="file"  name="slika required" accept="image/*"/>
    <p  class="alert alert-danger d-none">
        Ovo polje je obavezno!
    </p>
    <br><br>
    <button  @click = "addOrganizaciju(oIme, oOpis)">Dodaj organizaciju</button>
</div>
</div>
    `
});




