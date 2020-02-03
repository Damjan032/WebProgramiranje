Vue.component("diskovi",{
    data:function () {
        return{
            selektDisk:null,
            tipKorisnika:null,
            diskovi:[],
        }
    },
    mounted:function () {
        axios.get("/diskovi").then(response=>{
            this.diskovi = response.data;
        }).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });
        
        axios.get('/korisnik').then(response => {
            if(response.data==null){
                window.location.replace("/");
            }
            this.tipKorisnika = response.data.uloga;
        }).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });
    },
    methods:{

    },
    template:`
<div>
    <div class="container">
        <div class="page-header">
            <h2>Pregled diskova</h2>
        </div>
        <h4 v-if="diskovi.length==0">
            Trenutno nema diskova za prikaz
        </h4>
        <table v-else class="table">
            <tr class="thead-light">
                <th>
                    Ime
                </th>
                <th>
                    Kapacitet
                </th>
                <th>
                    Virtuelna mašina
                </th>
            </tr>
            <tr v-for="disk in diskovi">
                <td>
                    <router-link class = "block-link" :to="{name:'detaljiDiska', params:{disk:disk, tipKorisnika:tipKorisnika}}">
                        {{disk.ime}}
                    </router-link>
                </td>
                <td>
                    <router-link class = "block-link" :to="{name:'detaljiDiska', params:{disk:disk, tipKorisnika:tipKorisnika}}">
                        {{disk.kapacitet}}     
                    </router-link>
                </td>
                <td>
                    <router-link v-if="disk.vm" class = "block-link" :to="{name:'detaljiDiska', params:{disk:disk, tipKorisnika:tipKorisnika}}">
                        {{disk.vm.ime}}
                    </router-link>
                </td>
            </tr>
        </table>
        <router-link v-if = "tipKorisnika!='KORISNIK'" to="/dodajDisk">
            <button type="button" class="btn btn-success">
                Dodaj disk
            </button>
        </router-link>
    </div>
</div>
    `


});