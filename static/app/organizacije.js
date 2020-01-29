Vue.use(VueRouter);
const org = { template: '<org></org>' }
const detaljiOrg = { template: '<detalji-org></detalji-org>' }
const dodajOrg = { template: '<dodaj-org></dodaj-org>' }

const router = new VueRouter({
    routes: [
        { path: '/',
            component: org
        },
        { path: '/dodajOrg',
            component: dodajOrg
        },  
        { path: '/detaljiOrg/:org/:tipKorisnika',
            name:"detaljiOrg",
            component: detaljiOrg,
            props: true 
        }
    ]
});

let orgapp = new Vue({
    router,
    el:"#orgs"
});


//relikvija
// jQuery(document).ready(function($) {
//     $(".clickable-table-row").click(function() {
//         window.location = $(this).data("href");
//     });
// });

// let orgapp = new Vue({
//     el:"#orgs",
//     data: {
//         organizacije : null,
//         korisnikType : null
//     },
//     mounted () {
//         axios.get('/organizacije').then(response => {
//             this.organizacije = response.data;
//         }).catch(error=> {
//             let msg = error.response.data.ErrorMessage;
//             new Toast({
//                 message: msg,
//                 type: 'danger'
//             });
//         }); 
//         axios.get('/korisnik').then(response => {
//             this.korisnikType = response.data.uloga;
//         }); 
//     },
//     methods:{
        
//     }
// });



