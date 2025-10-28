from rest_framework.routers import DefaultRouter
from .views import DoctorViewSet, TimeSlotViewSet, PatientViewSet, AppointmentViewSet

router = DefaultRouter()
router.register(r'doctors', DoctorViewSet)
router.register(r'timeslots', TimeSlotViewSet)
router.register(r'patients', PatientViewSet)
router.register(r'appointments', AppointmentViewSet)

urlpatterns = router.urls
