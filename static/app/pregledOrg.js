Vue.component("pregled-org",{
    data:function () {
        return{                
            organizacije : null,
            korisnikType : null
        }
    },
    mounted:function () {
        axios.get('/organizacije').then(response => {
            this.organizacije = response.data;
        }).catch(error=> {
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message: msg,
                type: 'danger'
            });
        }); 
        axios.get('/korisnik').then(response => {
            if(response.data==null){
                window.location.replace("/");
            }
            this.korisnikType = response.data.uloga;
        }).catch(error=> {
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message: msg,
                type: 'danger'
            });
        });  
    },
    methods:{

    },
    template:`
<div>

<div class="page-header">
    <h2>Organizacije</h2>
</div>
<p>
<h3 v-if="!organizacije">
    Trenutno nema organizacija
</h3>
<table v-else class="table">
    <tr>
        <th>
            Logo
        </th>
        <th>
            Naziv
        </th>
        <th>
            Opis
        </th>
    </tr>

    <tr v-for = "o in organizacije" >
        <td>
            <router-link class = "block-link" :to="{name:'detaljiOrg', params:{org:o, tipKorisnika:korisnikType}}">
                <img width="50" height="50" v-bind:src="o.imgPath" class="rounded" >
            </router-link>
        </td>
        <td text-align="center">
            <router-link class = "block-link" :to="{name:'detaljiOrg', params:{org:o, tipKorisnika:korisnikType}}">

            <p>{{o.ime}}</p>
            </router-link>
        </td>
        <td text-align="center">
            <router-link class = "block-link" :to="{name:'detaljiOrg', params:{org:o, tipKorisnika:korisnikType}}">
                {{o.opis}}
            </router-link>
        </td>
    </tr>
    <tr>
        <td>
            <router-link v-if = "korisnikType=='SUPER_ADMIN'" to="/dodajOrg">
                <button type="button" class="btn btn-success">Dodaj organizaciju</button>
            </router-link>
        </td>
    </tr>
</table>
</p>

</div>
    `


});

