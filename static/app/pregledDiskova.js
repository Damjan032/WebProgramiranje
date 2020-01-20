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
        });
        
        axios.get('/korisnik').then(response => {
            this.tipKorisnika = response.data.uloga;
        });
    },
    methods:{

    },
    template:`
<div>
    <div class="jumbotron"><h1>Pregled diskova</h1></div>      
    <div class="container">
        <div class="page-header">
            <h2>Diskovi</h2>
        </div>
        <h4 v-if="!diskovi">
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
            <tr>
                <td></td>
                <td></td>
                <td>
                    <a v-if = "tipKorisnika!='KORISNIK'" href="dodajDisk.html">
                        <button type="button" class="btn btn-success">
                            Dodaj disk
                        </button>
                    </a>
                </td>
            </tr >
        </table>
    </div>
</div>
    `


});