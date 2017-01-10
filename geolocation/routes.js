const Router = require('express').Router;
const router = new Router();

var geoIpService = require('./modules/geo-ip/service');

router.route('/:ip')
	.get(function(req, res) {
		geoIpService.get(req.params.ip)
			.then(function(location) {
				res.json(location);
			})
	})

module.exports = router;
